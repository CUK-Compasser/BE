package Comprehensive_Design_Project.CUK_Compasser.domain.reservation.service;

import Comprehensive_Design_Project.CUK_Compasser.domain.randomBox.entity.RandomBox;
import Comprehensive_Design_Project.CUK_Compasser.domain.randomBox.repository.RandomBoxRepository;
import Comprehensive_Design_Project.CUK_Compasser.domain.reservation.converter.ReservationConverter;
import Comprehensive_Design_Project.CUK_Compasser.domain.reservation.dto.KakaoPayReqDTO;
import Comprehensive_Design_Project.CUK_Compasser.domain.reservation.dto.KakaoPayRespDTO;
import Comprehensive_Design_Project.CUK_Compasser.domain.reservation.dto.ReservationReqDTO;
import Comprehensive_Design_Project.CUK_Compasser.domain.reservation.dto.ReservationRespDTO;
import Comprehensive_Design_Project.CUK_Compasser.domain.reservation.entity.PaymentStatus;
import Comprehensive_Design_Project.CUK_Compasser.domain.reservation.entity.PickupStatus;
import Comprehensive_Design_Project.CUK_Compasser.domain.reservation.entity.Reservation;
import Comprehensive_Design_Project.CUK_Compasser.domain.reservation.entity.ReservationStatus;
import Comprehensive_Design_Project.CUK_Compasser.domain.reservation.repository.ReservationRepository;
import Comprehensive_Design_Project.CUK_Compasser.domain.store.entity.Store;
import Comprehensive_Design_Project.CUK_Compasser.domain.store.repository.StoreRepository;
import Comprehensive_Design_Project.CUK_Compasser.global.common.apiPayload.code.status.ErrorStatus;
import Comprehensive_Design_Project.CUK_Compasser.global.common.apiPayload.exception.GeneralException;
import Comprehensive_Design_Project.CUK_Compasser.global.config.KakaoPayProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final StoreRepository storeRepository;
    private final RandomBoxRepository randomBoxRepository;
    private final ReservationConverter reservationConverter;
    private final KakaoPayClient kakaoPayClient;
    private final KakaoPayProperties kakaoPayProperties;
    private final PlatformTransactionManager transactionManager;

    /**
     * 점장 확인 대기 목록 조회
     * - 로그인한 점장의 가게 기준으로 REQUESTED 상태 예약만 조회한다.
     */
    @Override
    @Transactional(readOnly = true)
    public ReservationRespDTO.ReservationListDTO getPendingReservations(Long memberId) {
        Store store = storeRepository.findByStoreManager_MemberId(memberId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.STORE_NOT_FOUND));

        List<Reservation> reservations =
                reservationRepository.findAllByStore_IdAndStatusOrderByCreatedAtDesc(
                        store.getId(), ReservationStatus.REQUESTED
                );

        return reservationConverter.toReservationListDTO(reservations);
    }

    /**
     * 점장 처리 완료 목록 조회
     * - APPROVED, REJECTED, CANCELED 상태 예약을 조회한다.
     */
    @Override
    @Transactional(readOnly = true)
    public ReservationRespDTO.ReservationListDTO getProcessedReservations(Long memberId) {
        Store store = storeRepository.findByStoreManager_MemberId(memberId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.STORE_NOT_FOUND));

        List<ReservationStatus> processedStatuses = List.of(
                ReservationStatus.APPROVED,
                ReservationStatus.REJECTED,
                ReservationStatus.CANCELED
        );

        List<Reservation> reservations =
                reservationRepository.findAllByStore_IdAndStatusInOrderByCreatedAtDesc(
                        store.getId(), processedStatuses
                );

        return reservationConverter.toReservationListDTO(reservations);
    }

    /**
     * 점장 예약 수락
     * - 선결제 후 수락 정책이므로 PAID 상태인 예약만 수락 가능하다.
     * - 수락 시 재고 차감 + pickupStatus 를 PREPARING 으로 변경한다.
     */
    @Override
    @Transactional
    public ReservationRespDTO.ReservationDTO approveReservation(Long reservationId, Long memberId) {
        Store store = storeRepository.findByStoreManager_MemberId(memberId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.STORE_NOT_FOUND));

        Reservation reservation = reservationRepository.findByIdAndStore_Id(reservationId, store.getId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.RESERVATION_NOT_FOUND));

        if (reservation.getStatus() != ReservationStatus.REQUESTED) {
            throw new GeneralException(ErrorStatus.INVALID_RESERVATION_STATUS);
        }

        if (reservation.getPaymentStatus() != PaymentStatus.PAID) {
            throw new GeneralException(ErrorStatus.INVALID_PAYMENT_STATUS);
        }

        RandomBox lockedRandomBox = randomBoxRepository.findWithLockById(reservation.getRandomBox().getId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.RANDOM_BOX_NOT_FOUND));

        lockedRandomBox.decreaseStock(reservation.getRequestedQuantity());

        reservation.approve();
        reservation.markPreparing();

        return reservationConverter.toReservationDTO(reservation);
    }

    /**
     * 점장 예약 거절
     * - 거절 사유는 필수이다.
     * - 이미 결제 완료된 예약은 카카오 결제 취소 후 REFUNDED 로 변경한다.
     * - 승인된 주문을 거절로 되돌리는 경우 재고를 복구한다.
     */
    @Override
    @Transactional
    public ReservationRespDTO.ReservationDTO rejectReservation(Long reservationId, Long memberId, ReservationReqDTO request) {
        Store store = storeRepository.findByStoreManager_MemberId(memberId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.STORE_NOT_FOUND));

        Reservation reservation = reservationRepository.findByIdAndStore_Id(reservationId, store.getId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.RESERVATION_NOT_FOUND));

        if (request.getRejectReason() == null || request.getRejectReason().trim().isEmpty()) {
            throw new GeneralException(ErrorStatus.REJECT_REASON_REQUIRED);
        }

        if (reservation.getPickupStatus() == PickupStatus.PICKED_UP) {
            throw new GeneralException(ErrorStatus.RESERVATION_ALREADY_PICKED_UP);
        }

        if (reservation.getStatus() == ReservationStatus.CANCELED ||
                reservation.getStatus() == ReservationStatus.REJECTED) {
            throw new GeneralException(ErrorStatus.INVALID_RESERVATION_STATUS);
        }

        if (reservation.getPaymentStatus() == PaymentStatus.REFUNDED) {
            throw new GeneralException(ErrorStatus.INVALID_PAYMENT_STATUS);
        }

        // 이미 승인된 주문을 거절로 되돌리는 경우 재고 복구
        if (reservation.getStatus() == ReservationStatus.APPROVED) {
            RandomBox lockedRandomBox = randomBoxRepository.findWithLockById(reservation.getRandomBox().getId())
                    .orElseThrow(() -> new GeneralException(ErrorStatus.RANDOM_BOX_NOT_FOUND));

            lockedRandomBox.increaseStock(reservation.getRequestedQuantity());
        }

        // 이미 결제 완료된 예약을 점장이 거절하면 환불 처리
        if (reservation.getPaymentStatus() == PaymentStatus.PAID) {
            cancelKakaoPayPayment(reservation);
            reservation.markRefunded();
        }

        reservation.reject(request.getRejectReason());

        return reservationConverter.toReservationDTO(reservation);
    }

    /**
     * 카카오페이 결제 준비
     * - READY 이전 단계(PENDING, FAILED, CANCELED 등)에서 다시 시도할 수 있다.
     * - 이미 PAID 인 경우 재결제 불가
     * - 취소/거절된 예약은 결제 불가
     */
    @Override
    @Transactional
    public KakaoPayRespDTO.ReadyResultDTO readyKakaoPay(Long reservationId, Long memberId) {
        Reservation reservation = reservationRepository.findByIdAndMember_Id(reservationId, memberId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.RESERVATION_NOT_FOUND));

        if (reservation.getStatus() == ReservationStatus.CANCELED ||
                reservation.getStatus() == ReservationStatus.REJECTED) {
            throw new GeneralException(ErrorStatus.INVALID_RESERVATION_STATUS);
        }

        if (reservation.getPaymentStatus() == PaymentStatus.PAID) {
            throw new GeneralException(ErrorStatus.RESERVATION_ALREADY_PAID);
        }

        String partnerOrderId = "reservation_" + reservation.getId();
        String partnerUserId = "member_" + memberId;

        KakaoPayReqDTO.ReadyRequestDTO request = KakaoPayReqDTO.ReadyRequestDTO.builder()
                .cid(kakaoPayProperties.getCid())
                .partner_order_id(partnerOrderId)
                .partner_user_id(partnerUserId)
                .item_name(reservation.getStore().getStoreName() + " 랜덤박스")
                .quantity(reservation.getRequestedQuantity())
                .total_amount(reservation.getTotalPrice())
                .tax_free_amount(0)
                .approval_url(kakaoPayProperties.getApprovalUrl() + "?reservationId=" + reservation.getId())
                .cancel_url(kakaoPayProperties.getCancelUrl() + "?reservationId=" + reservation.getId())
                .fail_url(kakaoPayProperties.getFailUrl() + "?reservationId=" + reservation.getId())
                .build();

        KakaoPayRespDTO.ReadyResponseDTO response = kakaoPayClient.ready(request);

        reservation.markPaymentReady(response.getTid(), "KAKAOPAY");

        return KakaoPayRespDTO.ReadyResultDTO.builder()
                .reservationId(reservation.getId())
                .tid(response.getTid())
                .redirectUrl(response.getNext_redirect_pc_url())
                .paymentStatus(reservation.getPaymentStatus().name())
                .build();
    }

    /**
     * 카카오페이 결제 승인
     * - READY 상태에서만 승인 가능하다.
     * - 승인 응답 검증 실패나 카카오 호출 실패 시 paymentStatus 를 FAILED 로 변경한다.
     */
    @Override
    @Transactional
    public KakaoPayRespDTO.ApproveResultDTO approveKakaoPay(Long reservationId, Long memberId, String pgToken) {
        if (!StringUtils.hasText(pgToken)) {
            throw new GeneralException(ErrorStatus.INVALID_PG_TOKEN);
        }

        Reservation reservation = reservationRepository.findByIdAndMember_Id(reservationId, memberId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.RESERVATION_NOT_FOUND));

        if (reservation.getStatus() == ReservationStatus.CANCELED ||
                reservation.getStatus() == ReservationStatus.REJECTED) {
            throw new GeneralException(ErrorStatus.INVALID_RESERVATION_STATUS);
        }

        if (!StringUtils.hasText(reservation.getPaymentTid())) {
            throw new GeneralException(ErrorStatus.PAYMENT_TID_NOT_FOUND);
        }

        if (reservation.getPaymentStatus() == PaymentStatus.PAID) {
            throw new GeneralException(ErrorStatus.RESERVATION_ALREADY_PAID);
        }

        if (reservation.getPaymentStatus() != PaymentStatus.READY) {
            throw new GeneralException(ErrorStatus.INVALID_PAYMENT_STATUS);
        }

        KakaoPayReqDTO.ApproveRequestDTO request = KakaoPayReqDTO.ApproveRequestDTO.builder()
                .cid(kakaoPayProperties.getCid())
                .tid(reservation.getPaymentTid())
                .partner_order_id("reservation_" + reservation.getId())
                .partner_user_id("member_" + memberId)
                .pg_token(pgToken)
                .build();

        try {
            KakaoPayRespDTO.ApproveResponseDTO response = kakaoPayClient.approve(request);

            validateApproveResponse(reservation, memberId, response);

            reservation.markPaid("KAKAOPAY");

            return KakaoPayRespDTO.ApproveResultDTO.builder()
                    .reservationId(reservation.getId())
                    .paymentMethod(reservation.getPaymentMethod())
                    .paymentStatus(reservation.getPaymentStatus().name())
                    .approvedAt(response.getApproved_at())
                    .build();

        }  catch (GeneralException e) {
            if (e.getCode() == ErrorStatus.KAKAOPAY_APPROVE_FAILED ||
                    e.getCode() == ErrorStatus.INVALID_PAYMENT_AMOUNT ||
                    e.getCode() == ErrorStatus.INVALID_PAYMENT_INFO) {
                markPaymentFailedInNewTx(reservation.getId());
            }
            throw e;

        } catch (Exception e) {
            markPaymentFailedInNewTx(reservation.getId());
            throw new GeneralException(ErrorStatus.KAKAOPAY_APPROVE_FAILED);
        }
    }

    /**
     * 카카오 결제창 취소 처리
     * - 실제 카카오 승인 완료(PAID) 전, READY 상태에서만 취소 가능하다.
     * - 취소 시 paymentStatus 를 CANCELED 로 변경한다.
     */
    @Override
    @Transactional
    public void cancelKakaoPay(Long reservationId, Long memberId) {
        Reservation reservation = reservationRepository.findByIdAndMember_Id(reservationId, memberId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.RESERVATION_NOT_FOUND));

        if (reservation.getPaymentStatus() == PaymentStatus.PAID) {
            throw new GeneralException(ErrorStatus.RESERVATION_ALREADY_PAID);
        }

        if (reservation.getPaymentStatus() != PaymentStatus.READY) {
            throw new GeneralException(ErrorStatus.INVALID_PAYMENT_STATUS);
        }

        reservation.cancelPayment();
    }

    /**
     * 카카오페이 결제 취소(환불)
     * - 이미 결제 완료된 reservation 에 대해서만 호출한다.
     * - 카카오 취소 응답의 tid 가 기존 결제 tid 와 일치하는지 검증한다.
     */
    private void cancelKakaoPayPayment(Reservation reservation) {
        if (!StringUtils.hasText(reservation.getPaymentTid())) {
            throw new GeneralException(ErrorStatus.PAYMENT_TID_NOT_FOUND);
        }

        KakaoPayReqDTO.CancelReqDTO request = KakaoPayReqDTO.CancelReqDTO.builder()
                .cid(kakaoPayProperties.getCid())
                .tid(reservation.getPaymentTid())
                .cancel_amount(reservation.getTotalPrice())
                .cancel_tax_free_amount(0)
                .build();

        KakaoPayRespDTO.CancelRespDTO response = kakaoPayClient.cancel(request);

        if (response == null || !reservation.getPaymentTid().equals(response.getTid())) {
            throw new GeneralException(ErrorStatus.INVALID_PAYMENT_INFO);
        }
    }

    /**
     * 승인 실패 상태를 별도 트랜잭션으로 반영
     */
    private void markPaymentFailedInNewTx(Long reservationId) {
        TransactionTemplate txTemplate = new TransactionTemplate(transactionManager);
        txTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);

        txTemplate.executeWithoutResult(status -> {
            Reservation failedReservation = reservationRepository.findById(reservationId)
                    .orElseThrow(() -> new GeneralException(ErrorStatus.RESERVATION_NOT_FOUND));

            failedReservation.markPaymentFailed();
        });
    }

    /**
     * 카카오 승인 응답 검증
     * - amount, tid, partner_order_id, partner_user_id, cid 를 검증한다.
     */
    private void validateApproveResponse(Reservation reservation,
                                         Long memberId,
                                         KakaoPayRespDTO.ApproveResponseDTO response) {
        if (response == null || response.getAmount() == null || response.getAmount().getTotal() == null) {
            throw new GeneralException(ErrorStatus.INVALID_PAYMENT_AMOUNT);
        }

        Integer approvedAmount = response.getAmount().getTotal();
        Integer expectedAmount = reservation.getTotalPrice();

        if (!expectedAmount.equals(approvedAmount)) {
            throw new GeneralException(ErrorStatus.INVALID_PAYMENT_AMOUNT);
        }

        if (!reservation.getPaymentTid().equals(response.getTid())) {
            throw new GeneralException(ErrorStatus.INVALID_PAYMENT_INFO);
        }

        String expectedPartnerOrderId = "reservation_" + reservation.getId();
        if (!expectedPartnerOrderId.equals(response.getPartner_order_id())) {
            throw new GeneralException(ErrorStatus.INVALID_PAYMENT_INFO);
        }

        String expectedPartnerUserId = "member_" + memberId;
        if (!expectedPartnerUserId.equals(response.getPartner_user_id())) {
            throw new GeneralException(ErrorStatus.INVALID_PAYMENT_INFO);
        }

        if (!kakaoPayProperties.getCid().equals(response.getCid())) {
            throw new GeneralException(ErrorStatus.INVALID_PAYMENT_INFO);
        }
    }
}