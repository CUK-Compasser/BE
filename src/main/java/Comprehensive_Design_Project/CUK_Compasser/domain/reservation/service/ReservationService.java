package Comprehensive_Design_Project.CUK_Compasser.domain.reservation.service;

import Comprehensive_Design_Project.CUK_Compasser.domain.reservation.dto.ReservationReqDTO;
import Comprehensive_Design_Project.CUK_Compasser.domain.reservation.dto.ReservationRespDTO;
import Comprehensive_Design_Project.CUK_Compasser.domain.reservation.entity.ReservationStatus;

public interface ReservationService {

    ReservationRespDTO.ReservationListDTO getReservations(Long storeId, Long memberId, ReservationStatus status);

    ReservationRespDTO.ReservationDTO getReservationDetail(Long storeId, Long reservationId, Long memberId);

    ReservationRespDTO.ReservationDTO rejectReservation(Long reservationId, Long memberId,
                                                        ReservationReqDTO.RejectDTO request);
}