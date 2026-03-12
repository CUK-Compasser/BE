package Comprehensive_Design_Project.CUK_Compasser.domain.reservation.service;

import Comprehensive_Design_Project.CUK_Compasser.domain.reservation.converter.ReservationConverter;
import Comprehensive_Design_Project.CUK_Compasser.domain.reservation.dto.ReservationReqDTO;
import Comprehensive_Design_Project.CUK_Compasser.domain.reservation.dto.ReservationRespDTO;
import Comprehensive_Design_Project.CUK_Compasser.domain.reservation.entity.Reservation;
import Comprehensive_Design_Project.CUK_Compasser.domain.reservation.entity.ReservationStatus;
import Comprehensive_Design_Project.CUK_Compasser.domain.reservation.repository.ReservationRepository;
import Comprehensive_Design_Project.CUK_Compasser.domain.store.entity.Store;
import Comprehensive_Design_Project.CUK_Compasser.domain.store.repository.StoreRepository;
import Comprehensive_Design_Project.CUK_Compasser.domain.storeManager.repository.StoreManagerRepository;
import Comprehensive_Design_Project.CUK_Compasser.global.common.apiPayload.code.status.ErrorStatus;
import Comprehensive_Design_Project.CUK_Compasser.global.common.apiPayload.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final StoreRepository storeRepository;
    private final StoreManagerRepository storeManagerRepository;
    private final ReservationConverter reservationConverter;

    @Override
    public ReservationRespDTO.ReservationListDTO getPendingReservations(Long memberId) {
        validateStoreManager(memberId);
        Store store = getOwnerStore(memberId);

        List<Reservation> reservations =
                reservationRepository.findAllByStore_IdAndStatusOrderByCreatedAtDesc(
                        store.getId(), ReservationStatus.REQUESTED
                );

        return reservationConverter.toReservationListDTO(reservations);
    }

    @Override
    public ReservationRespDTO.ReservationListDTO getProcessedReservations(Long memberId) {
        validateStoreManager(memberId);
        Store store = getOwnerStore(memberId);

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
        validateStoreManager(memberId);
        Store store = getOwnerStore(memberId);

        Reservation reservation = reservationRepository.findByIdAndStore_Id(reservationId, store.getId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.RESERVATION_NOT_FOUND));

        validateRequestableReservation(reservation);

        reservation.setStatus(ReservationStatus.APPROVED);
        reservation.setRejectReason(null);

        return reservationConverter.toReservationDTO(reservation);
    }

    @Override
    @Transactional
    public ReservationRespDTO.ReservationDTO rejectReservation(Long reservationId, Long memberId,
                                                               ReservationReqDTO.RejectDTO request) {
        validateStoreManager(memberId);
        Store store = getOwnerStore(memberId);

        Reservation reservation = reservationRepository.findByIdAndStore_Id(reservationId, store.getId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.RESERVATION_NOT_FOUND));

        validateRequestableReservation(reservation);

        if (request == null || !StringUtils.hasText(request.getRejectReason())) {
            throw new GeneralException(ErrorStatus.REJECT_REASON_REQUIRED);
        }

        reservation.setStatus(ReservationStatus.REJECTED);
        reservation.setRejectReason(request.getRejectReason().trim());

        return reservationConverter.toReservationDTO(reservation);
    }

    private void validateStoreManager(Long memberId) {
        if (!storeManagerRepository.existsById(memberId)) {
            throw new GeneralException(ErrorStatus.STORE_MANAGER_NOT_FOUND);
        }
    }

    private Store getOwnerStore(Long memberId) {
        return storeRepository.findByStoreManager_MemberId(memberId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.STORE_NOT_FOUND));
    }

    private void validateRequestableReservation(Reservation reservation) {
        if (reservation.getStatus() != ReservationStatus.REQUESTED) {
            throw new GeneralException(ErrorStatus.RESERVATION_ALREADY_PROCESSED);
        }
    }
}