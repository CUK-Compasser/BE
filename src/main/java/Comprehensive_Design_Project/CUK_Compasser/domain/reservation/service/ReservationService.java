package Comprehensive_Design_Project.CUK_Compasser.domain.reservation.service;

import Comprehensive_Design_Project.CUK_Compasser.domain.reservation.dto.KakaoPayRespDTO;
import Comprehensive_Design_Project.CUK_Compasser.domain.reservation.dto.ReservationReqDTO;
import Comprehensive_Design_Project.CUK_Compasser.domain.reservation.dto.ReservationRespDTO;

public interface ReservationService {

    ReservationRespDTO.ReservationListDTO getPendingReservations(Long memberId);

    ReservationRespDTO.ReservationListDTO getProcessedReservations(Long memberId);

    ReservationRespDTO.ReservationDTO approveReservation(Long reservationId, Long memberId);

    ReservationRespDTO.ReservationDTO rejectReservation(Long reservationId, Long memberId, ReservationReqDTO request);

    KakaoPayRespDTO.ReadyResultDTO readyKakaoPay(Long reservationId, Long memberId);

    KakaoPayRespDTO.ApproveResultDTO approveKakaoPay(Long reservationId, Long memberId, String pgToken);

    void cancelKakaoPay(Long reservationId, Long memberId);
}