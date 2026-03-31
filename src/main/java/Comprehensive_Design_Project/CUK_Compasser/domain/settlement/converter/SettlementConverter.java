package Comprehensive_Design_Project.CUK_Compasser.domain.settlement.converter;

import Comprehensive_Design_Project.CUK_Compasser.domain.reservation.entity.Reservation;
import Comprehensive_Design_Project.CUK_Compasser.domain.settlement.dto.SettlementRespDTO;
import Comprehensive_Design_Project.CUK_Compasser.domain.store.entity.Store;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SettlementConverter {

    public SettlementRespDTO.SettlementPreviewDTO toSettlementPreviewDTO(Store store, List<Reservation> reservations) {
        int totalAmount = reservations.stream()
                .mapToInt(Reservation::getTotalPrice)
                .sum();

        List<SettlementRespDTO.SettlementReservationDTO> items = reservations.stream()
                .map(this::toSettlementReservationDTO)
                .toList();

        return SettlementRespDTO.SettlementPreviewDTO.builder()
                .storeId(store.getId())
                .storeName(store.getStoreName())
                .count(reservations.size())
                .totalAmount(totalAmount)
                .reservations(items)
                .build();
    }

    public SettlementRespDTO.SettlementReservationDTO toSettlementReservationDTO(Reservation reservation) {
        return SettlementRespDTO.SettlementReservationDTO.builder()
                .reservationId(reservation.getId())
                .memberId(reservation.getMember().getId())
                .totalPrice(reservation.getTotalPrice())
                .createdAt(reservation.getCreatedAt())
                .build();
    }

    public SettlementRespDTO.SettlementCompleteDTO toSettlementCompleteDTO(Store store, List<Reservation> reservations) {
        int totalAmount = reservations.stream()
                .mapToInt(Reservation::getTotalPrice)
                .sum();

        return SettlementRespDTO.SettlementCompleteDTO.builder()
                .storeId(store.getId())
                .storeName(store.getStoreName())
                .count(reservations.size())
                .totalAmount(totalAmount)
                .message("정산 완료 처리되었습니다.")
                .build();
    }
}