package Comprehensive_Design_Project.CUK_Compasser.domain.reservation.service;

import Comprehensive_Design_Project.CUK_Compasser.domain.randomBox.entity.RandomBox;
import Comprehensive_Design_Project.CUK_Compasser.domain.randomBox.repository.RandomBoxRepository;
import Comprehensive_Design_Project.CUK_Compasser.domain.reservation.converter.ReservationConverter;
import Comprehensive_Design_Project.CUK_Compasser.domain.reservation.dto.KakaoPayReqDTO;
import Comprehensive_Design_Project.CUK_Compasser.domain.reservation.dto.KakaoPayRespDTO;
import Comprehensive_Design_Project.CUK_Compasser.domain.reservation.dto.ReservationReqDTO;
import Comprehensive_Design_Project.CUK_Compasser.domain.reservation.dto.ReservationRespDTO;
import Comprehensive_Design_Project.CUK_Compasser.domain.reservation.entity.PaymentStatus;
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

        return reservationConverter.toReservationDTO(reservation);
    }

    /**
     * 점장 예약 거절
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

        if (reservation.getStatus() == ReservationStatus.CANCELED ||
                reservation.getStatus() == ReservationStatus.REJECTED) {
            throw new GeneralException(ErrorStatus.INVALID_RESERVATION_STATUS);
        }

        if (reservation.getPaymentStatus() == PaymentStatus.REFUNDED) {
            throw new GeneralException(ErrorStatus.INVALID_PAYMENT_STATUS);
        }

        if (reservation.getStatus() == ReservationStatus.APPROVED) {
            RandomBox lockedRandomBox = randomBoxRepository.findWithLockById(reservation.getRandomBox().getId())
                    .orElseThrow(() -> new GeneralException(ErrorStatus.RANDOM_BOX_NOT_FOUND));
            lockedRandomBox.increaseStock(reservation.getRequestedQuantity());
        }

        if (reservation.getPaymentStatus() == PaymentStatus.PAID) {
            validateRefundable(reservation);
            cancelKakaoPayPayment(reservation);
            reservation.markRefunded();
        }

        reservation.reject(request.getRejectReason());

        return reservationConverter.toReservationDTO(reservation);
    }

    /**
     * 카카오페이 결제 준비
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

        KakaoPayReqDTO.ReadyRequestDTO request = KakaoPayReqDTO.ReadyRequestDTO.builder()
                .cid(kakaoPayProperties.getCid())
                .partner_order_id("reservation_" + reservation.getId())
                .partner_user_id("member_" + memberId)
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
     * 카카오페이 결제 승인 (프론트 직접 호출용 - 현재는 미사용, 콜백 방식으로 대체됨)
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

        } catch (GeneralException e) {
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
     * 카카오페이 콜백 - 결제 승인 (서버 내부 처리용)
     * - 카카오페이 success 콜백에서 자동 호출, 인증 없음
     */
    @Override
    @Transactional
    public void approveKakaoPayByCallback(Long reservationId, String pgToken) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.RESERVATION_NOT_FOUND));

        if (reservation.getPaymentStatus() != PaymentStatus.READY) {
            throw new GeneralException(ErrorStatus.INVALID_PAYMENT_STATUS);
        }

        if (!StringUtils.hasText(reservation.getPaymentTid())) {
            throw new GeneralException(ErrorStatus.PAYMENT_TID_NOT_FOUND);
        }

        Long memberId = reservation.getMember().getId();

        KakaoPayReqDTO.ApproveRequestDTO request = KakaoPayReqDTO.ApproveRequestDTO.builder()
                .cid(kakaoPayProperties.getCid())
                .tid(reservation.getPaymentTid())
                .partner_order_id("reservation_" + reservationId)
                .partner_user_id("member_" + memberId)
                .pg_token(pgToken)
                .build();

        try {
            KakaoPayRespDTO.ApproveResponseDTO response = kakaoPayClient.approve(request);
            validateApproveResponse(reservation, memberId, response);
            reservation.markPaid("KAKAOPAY");

        } catch (GeneralException e) {
            if (e.getCode() == ErrorStatus.KAKAOPAY_APPROVE_FAILED ||
                    e.getCode() == ErrorStatus.INVALID_PAYMENT_AMOUNT ||
                    e.getCode() == ErrorStatus.INVALID_PAYMENT_INFO) {
                markPaymentFailedInNewTx(reservationId);
            }
            throw e;
        } catch (Exception e) {
            markPaymentFailedInNewTx(reservationId);
            throw new GeneralException(ErrorStatus.KAKAOPAY_APPROVE_FAILED);
        }
    }

    /**
     * 카카오 결제창 취소 처리 (프론트 직접 호출용)
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
     * 카카오페이 콜백 - 결제 취소 상태 반영
     */
    @Override
    @Transactional
    public void cancelKakaoPayByCallback(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.RESERVATION_NOT_FOUND));

        if (reservation.getPaymentStatus() == PaymentStatus.READY) {
            reservation.cancelPayment();
        }
    }

    /**
     * 카카오페이 콜백 - 결제 실패 상태 반영
     */
    @Override
    @Transactional
    public void markPaymentFailedByCallback(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.RESERVATION_NOT_FOUND));

        if (reservation.getPaymentStatus() == PaymentStatus.READY) {
            reservation.markPaymentFailed();
        }
    }

    /**
     * 카카오페이 결제 취소(환불) - 점장 거절 시 내부 호출
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
     */
    private void validateApproveResponse(Reservation reservation,
                                         Long memberId,
                                         KakaoPayRespDTO.ApproveResponseDTO response) {
        if (response == null || response.getAmount() == null || response.getAmount().getTotal() == null) {
            throw new GeneralException(ErrorStatus.INVALID_PAYMENT_AMOUNT);
        }

        if (!reservation.getTotalPrice().equals(response.getAmount().getTotal())) {
            throw new GeneralException(ErrorStatus.INVALID_PAYMENT_AMOUNT);
        }

        if (!reservation.getPaymentTid().equals(response.getTid())) {
            throw new GeneralException(ErrorStatus.INVALID_PAYMENT_INFO);
        }

        if (!("reservation_" + reservation.getId()).equals(response.getPartner_order_id())) {
            throw new GeneralException(ErrorStatus.INVALID_PAYMENT_INFO);
        }

        if (!("member_" + memberId).equals(response.getPartner_user_id())) {
            throw new GeneralException(ErrorStatus.INVALID_PAYMENT_INFO);
        }

        if (!kakaoPayProperties.getCid().equals(response.getCid())) {
            throw new GeneralException(ErrorStatus.INVALID_PAYMENT_INFO);
        }
    }

    private void validateRefundable(Reservation reservation) {
        if (Boolean.TRUE.equals(reservation.getSettled())) {
            throw new GeneralException(ErrorStatus.REFUND_NOT_ALLOWED_AFTER_SETTLEMENT);
        }
    }
}