package Comprehensive_Design_Project.CUK_Compasser.domain.reservation.converter;

import Comprehensive_Design_Project.CUK_Compasser.domain.reservation.dto.ReservationRespDTO;
import Comprehensive_Design_Project.CUK_Compasser.domain.reservation.entity.Reservation;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ReservationConverter {

    public ReservationRespDTO.ReservationDTO toReservationDTO(Reservation reservation) {
        return ReservationRespDTO.ReservationDTO.builder()
                .reservationId(reservation.getId())
                .memberId(reservation.getMember().getId())
                .customerName(reservation.getMember().getMemberName())
                .storeId(reservation.getStore().getId())
                .storeName(reservation.getStore().getStoreName())
                .randomBoxId(reservation.getRandomBox().getId())
                .randomBoxName(reservation.getRandomBox().getBoxName())
                .totalPrice(reservation.getTotalPrice())
                .status(reservation.getStatus())
                .requestedQuantity(reservation.getRequestedQuantity())
                .rejectReason(reservation.getRejectReason())
                .build();
    }

    public ReservationRespDTO.ReservationListDTO toReservationListDTO(List<Reservation> reservations) {
        List<ReservationRespDTO.ReservationDTO> reservationDTOList = reservations.stream()
                .map(this::toReservationDTO)
                .toList();

        return ReservationRespDTO.ReservationListDTO.builder()
                .reservations(reservationDTOList)
                .build();
    }
}