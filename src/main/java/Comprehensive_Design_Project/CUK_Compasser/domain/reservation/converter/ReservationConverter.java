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
                .memberName(reservation.getMember().getMemberName())
                .storeId(reservation.getStore().getId())
                .storeName(reservation.getStore().getStoreName())
                .status(reservation.getStatus())
                .requestedQuantity(reservation.getRequestedQuantity())
                .rejectReason(reservation.getRejectReason())
                .createdAt(reservation.getCreatedAt())
                .updatedAt(reservation.getUpdatedAt())
                .build();
    }

    public ReservationRespDTO.ReservationListDTO toReservationListDTO(List<Reservation> reservations) {
        List<ReservationRespDTO.ReservationDTO> dtoList = reservations.stream()
                .map(this::toReservationDTO)
                .toList();

        return ReservationRespDTO.ReservationListDTO.builder()
                .reservations(dtoList)
                .count(dtoList.size())
                .build();
    }
}