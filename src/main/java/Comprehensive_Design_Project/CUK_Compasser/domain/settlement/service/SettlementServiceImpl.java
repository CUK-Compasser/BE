package Comprehensive_Design_Project.CUK_Compasser.domain.settlement.service;

import Comprehensive_Design_Project.CUK_Compasser.domain.reservation.entity.PaymentStatus;
import Comprehensive_Design_Project.CUK_Compasser.domain.reservation.entity.Reservation;
import Comprehensive_Design_Project.CUK_Compasser.domain.reservation.entity.ReservationStatus;
import Comprehensive_Design_Project.CUK_Compasser.domain.reservation.repository.ReservationRepository;
import Comprehensive_Design_Project.CUK_Compasser.domain.settlement.converter.SettlementConverter;
import Comprehensive_Design_Project.CUK_Compasser.domain.settlement.dto.SettlementReqDTO;
import Comprehensive_Design_Project.CUK_Compasser.domain.settlement.dto.SettlementRespDTO;
import Comprehensive_Design_Project.CUK_Compasser.domain.store.entity.Store;
import Comprehensive_Design_Project.CUK_Compasser.domain.store.repository.StoreRepository;
import Comprehensive_Design_Project.CUK_Compasser.global.common.apiPayload.code.status.ErrorStatus;
import Comprehensive_Design_Project.CUK_Compasser.global.common.apiPayload.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SettlementServiceImpl implements SettlementService {

    private final StoreRepository storeRepository;
    private final ReservationRepository reservationRepository;
    private final SettlementConverter settlementConverter;

    @Override
    public SettlementRespDTO.SettlementPreviewDTO previewSettlement(Long ownerId) {
        Store store = storeRepository.findByStoreManager_MemberId(ownerId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.STORE_NOT_FOUND));

        List<Reservation> targets =
                reservationRepository.findAllByStore_IdAndStatusAndPaymentStatusAndSettledFalseOrderByCreatedAtAsc(
                        store.getId(),
                        ReservationStatus.APPROVED,
                        PaymentStatus.PAID
                );

        return settlementConverter.toSettlementPreviewDTO(store, targets);
    }

    @Override
    @Transactional
    public SettlementRespDTO.SettlementCompleteDTO completeSettlementByStore(Long ownerId, SettlementReqDTO.CompleteSettlementDTO request) {
        if (request.getReservationIds() == null || request.getReservationIds().isEmpty()) {
            throw new GeneralException(ErrorStatus.INVALID_SETTLEMENT_REQUEST);
        }

        Store store = storeRepository.findByStoreManager_MemberId(ownerId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.STORE_NOT_FOUND));

        List<Reservation> reservations =
                reservationRepository.findAllByIdInAndStore_Id(request.getReservationIds(), store.getId());

        if (reservations.isEmpty()) {
            throw new GeneralException(ErrorStatus.SETTLEMENT_TARGET_NOT_FOUND);
        }

        for (Reservation reservation : reservations) {
            validateSettlementTarget(reservation);
        }

        reservations.forEach(Reservation::markSettled);

        return settlementConverter.toSettlementCompleteDTO(store, reservations);
    }

    private void validateSettlementTarget(Reservation reservation) {
        if (reservation.getStatus() != ReservationStatus.APPROVED) {
            throw new GeneralException(ErrorStatus.INVALID_RESERVATION_STATUS);
        }

        if (reservation.getPaymentStatus() != PaymentStatus.PAID) {
            throw new GeneralException(ErrorStatus.INVALID_PAYMENT_STATUS);
        }

        if (Boolean.TRUE.equals(reservation.getSettled())) {
            throw new GeneralException(ErrorStatus.ALREADY_SETTLED);
        }
    }
}