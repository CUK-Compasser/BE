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
    public ReservationRespDTO.ReservationListDTO getReservations(Long storeId, Long memberId, ReservationStatus status) {
        validateStoreManager(memberId);
        validateStoreOwner(storeId, memberId);

        List<Reservation> reservations = (status == null)
                ? reservationRepository.findAllByStore_IdOrderByCreatedAtDesc(storeId)
                : reservationRepository.findAllByStore_IdAndStatusOrderByCreatedAtDesc(storeId, status);

        return reservationConverter.toReservationListDTO(reservations);
    }

    @Override
    public ReservationRespDTO.ReservationDetailDTO getReservationDetail(Long storeId, Long reservationId, Long memberId) {
        validateStoreManager(memberId);
        validateStoreOwner(storeId, memberId);

        Reservation reservation = reservationRepository.findByIdAndStore_Id(reservationId, storeId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.RESERVATION_NOT_FOUND));

        return reservationConverter.toReservationDetailDTO(reservation);
    }

    @Override
    @Transactional
    public ReservationRespDTO.ReservationDetailDTO approveReservation(Long reservationId, Long memberId,
                                                                      ReservationReqDTO.ApproveDTO request) {
        validateStoreManager(memberId);

        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.RESERVATION_NOT_FOUND));

        validateStoreOwner(reservation.getStore().getId(), memberId);
        validateRequestableReservation(reservation);

        if (request.getApprovedQuantity() == null || request.getApprovedQuantity() <= 0) {
            throw new GeneralException(ErrorStatus.INVALID_APPROVED_QUANTITY);
        }

        if (request.getApprovedQuantity() > reservation.getRequestedQuantity()) {
            throw new GeneralException(ErrorStatus.APPROVED_QUANTITY_EXCEEDS_REQUESTED);
        }

        reservation.setStatus(ReservationStatus.APPROVED);
        reservation.setApprovedQuantity(request.getApprovedQuantity());
        reservation.setMemo(request.getMemo());
        reservation.setRejectReason(null);

        return reservationConverter.toReservationDetailDTO(reservation);
    }

    @Override
    @Transactional
    public ReservationRespDTO.ReservationDetailDTO rejectReservation(Long reservationId, Long memberId,
                                                                     ReservationReqDTO.RejectDTO request) {
        validateStoreManager(memberId);

        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.RESERVATION_NOT_FOUND));

        validateStoreOwner(reservation.getStore().getId(), memberId);
        validateRequestableReservation(reservation);

        if (!StringUtils.hasText(request.getRejectReason())) {
            throw new GeneralException(ErrorStatus.REJECT_REASON_REQUIRED);
        }

        reservation.setStatus(ReservationStatus.REJECTED);
        reservation.setRejectReason(request.getRejectReason());
        reservation.setApprovedQuantity(null);
        reservation.setMemo(null);

        return reservationConverter.toReservationDetailDTO(reservation);
    }

    private void validateStoreManager(Long memberId) {
        if (!storeManagerRepository.existsById(memberId)) {
            throw new GeneralException(ErrorStatus.STORE_MANAGER_NOT_FOUND);
        }
    }

    private void validateStoreOwner(Long storeId, Long memberId) {
        Store store = storeRepository.findByIdAndStoreManager_MemberId(storeId, memberId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.STORE_FORBIDDEN));
    }

    private void validateRequestableReservation(Reservation reservation) {
        if (reservation.getStatus() != ReservationStatus.REQUESTED) {
            throw new GeneralException(ErrorStatus.RESERVATION_ALREADY_PROCESSED);
        }
    }
}