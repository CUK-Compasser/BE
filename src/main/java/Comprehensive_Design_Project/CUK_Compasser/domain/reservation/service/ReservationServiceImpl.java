package Comprehensive_Design_Project.CUK_Compasser.domain.reservation.service;

import Comprehensive_Design_Project.CUK_Compasser.domain.randomBox.entity.RandomBox;
import Comprehensive_Design_Project.CUK_Compasser.domain.randomBox.repository.RandomBoxRepository;
import Comprehensive_Design_Project.CUK_Compasser.domain.reservation.converter.ReservationConverter;
import Comprehensive_Design_Project.CUK_Compasser.domain.reservation.dto.KakaoPayReqDTO;
import Comprehensive_Design_Project.CUK_Compasser.domain.reservation.dto.KakaoPayRespDTO;
import Comprehensive_Design_Project.CUK_Compasser.domain.reservation.dto.ReservationReqDTO;
import Comprehensive_Design_Project.CUK_Compasser.domain.reservation.dto.ReservationRespDTO;
import Comprehensive_Design_Project.CUK_Compasser.domain.reservation.entity.*;
import Comprehensive_Design_Project.CUK_Compasser.domain.reservation.repository.ReservationRepository;
import Comprehensive_Design_Project.CUK_Compasser.domain.store.entity.Store;
import Comprehensive_Design_Project.CUK_Compasser.domain.store.repository.StoreRepository;
import Comprehensive_Design_Project.CUK_Compasser.global.common.apiPayload.code.status.ErrorStatus;
import Comprehensive_Design_Project.CUK_Compasser.global.common.apiPayload.exception.GeneralException;
import Comprehensive_Design_Project.CUK_Compasser.global.config.KakaoPayProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
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

    @Lazy
    private final ReservationServiceImpl self;

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

    @Override
    @Transactional
    public ReservationRespDTO.ReservationDTO approveReservation(Long reservationId, Long memberId) {
        Store store = storeRepository.findByStoreManager_MemberId(memberId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.STORE_NOT_FOUND));

        Reservation reservation = reservationRepository.findByIdAndStore_Id(reservationId, store.getId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.RESERVATION_NOT_FOUND));

        ReservationStatus currentStatus = reservation.getStatus();

        if (reservation.getPickupStatus() == PickupStatus.PICKED_UP) {
            throw new GeneralException(ErrorStatus.RESERVATION_ALREADY_PICKED_UP);
        }

        if (currentStatus == ReservationStatus.CANCELED) {
            throw new GeneralException(ErrorStatus.INVALID_RESERVATION_STATUS);
        }

        if (currentStatus == ReservationStatus.APPROVED) {
            throw new GeneralException(ErrorStatus.RESERVATION_ALREADY_APPROVED);
        }

        RandomBox lockedRandomBox = randomBoxRepository.findWithLockById(reservation.getRandomBox().getId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.RANDOM_BOX_NOT_FOUND));

        lockedRandomBox.decreaseStock(reservation.getRequestedQuantity());

        reservation.approve();
        reservation.markPreparing();

        return reservationConverter.toReservationDTO(reservation);
    }

    @Override
    @Transactional
    public ReservationRespDTO.ReservationDTO rejectReservation(Long reservationId, Long memberId, ReservationReqDTO request) {
        Store store = storeRepository.findByStoreManager_MemberId(memberId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.STORE_NOT_FOUND));

        Reservation reservation = reservationRepository.findByIdAndStore_Id(reservationId, store.getId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.RESERVATION_NOT_FOUND));

        ReservationStatus currentStatus = reservation.getStatus();

        if (request.getRejectReason() == null || request.getRejectReason().trim().isEmpty()) {
            throw new GeneralException(ErrorStatus.REJECT_REASON_REQUIRED);
        }

        if (reservation.getPickupStatus() == PickupStatus.PICKED_UP) {
            throw new GeneralException(ErrorStatus.RESERVATION_ALREADY_PICKED_UP);
        }

        if (currentStatus == ReservationStatus.CANCELED) {
            throw new GeneralException(ErrorStatus.INVALID_RESERVATION_STATUS);
        }

        if (currentStatus == ReservationStatus.REJECTED) {
            throw new GeneralException(ErrorStatus.RESERVATION_ALREADY_REJECTED);
        }

        if (currentStatus == ReservationStatus.APPROVED) {
            RandomBox lockedRandomBox = randomBoxRepository.findWithLockById(reservation.getRandomBox().getId())
                    .orElseThrow(() -> new GeneralException(ErrorStatus.RANDOM_BOX_NOT_FOUND));

            lockedRandomBox.increaseStock(reservation.getRequestedQuantity());
        }

        reservation.reject(request.getRejectReason());

        return reservationConverter.toReservationDTO(reservation);
    }

    @Override
    @Transactional
    public KakaoPayRespDTO.ReadyResultDTO readyKakaoPay(Long reservationId, Long memberId) {
        Reservation reservation = reservationRepository.findByIdAndMember_Id(reservationId, memberId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.RESERVATION_NOT_FOUND));

        if (reservation.getPaymentStatus() == PaymentStatus.PAID) {
            throw new GeneralException(ErrorStatus.RESERVATION_ALREADY_PAID);
        }

        if (reservation.getStatus() == ReservationStatus.CANCELED ||
                reservation.getStatus() == ReservationStatus.REJECTED) {
            throw new GeneralException(ErrorStatus.INVALID_RESERVATION_STATUS);
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

        try {
            KakaoPayReqDTO.ApproveRequestDTO request = KakaoPayReqDTO.ApproveRequestDTO.builder()
                    .cid(kakaoPayProperties.getCid())
                    .tid(reservation.getPaymentTid())
                    .partner_order_id("reservation_" + reservation.getId())
                    .partner_user_id("member_" + memberId)
                    .pg_token(pgToken)
                    .build();

            KakaoPayRespDTO.ApproveResponseDTO response = kakaoPayClient.approve(request);

            validateApproveResponse(reservation, response);

            reservation.markPaid("KAKAOPAY");

            return KakaoPayRespDTO.ApproveResultDTO.builder()
                    .reservationId(reservation.getId())
                    .paymentMethod(reservation.getPaymentMethod())
                    .paymentStatus(reservation.getPaymentStatus().name())
                    .approvedAt(response.getApproved_at())
                    .build();

        } catch (GeneralException e) {
            self.markPaymentFailedInNewTx(reservation.getId());
            throw e;
        } catch (Exception e) {
            self.markPaymentFailedInNewTx(reservation.getId());
            throw new GeneralException(ErrorStatus.KAKAOPAY_APPROVE_FAILED);
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void markPaymentFailedInNewTx(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.RESERVATION_NOT_FOUND));

        reservation.markPaymentFailed();
    }

    private void validateApproveResponse(Reservation reservation, KakaoPayRespDTO.ApproveResponseDTO response) {
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
    }

    @Override
    @Transactional
    public void cancelKakaoPay(Long reservationId, Long memberId) {
        Reservation reservation = reservationRepository.findByIdAndMember_Id(reservationId, memberId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.RESERVATION_NOT_FOUND));

        if (reservation.getPaymentStatus() == PaymentStatus.PAID) {
            throw new GeneralException(ErrorStatus.RESERVATION_ALREADY_PAID);
        }

        reservation.cancelPayment();
    }
}