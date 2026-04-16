package Comprehensive_Design_Project.CUK_Compasser.domain.order.converter;

import Comprehensive_Design_Project.CUK_Compasser.domain.order.dto.OrderRespDTO;
import Comprehensive_Design_Project.CUK_Compasser.domain.reservation.entity.Reservation;

import java.util.List;

public class OrderConverter {

    private OrderConverter() {
    }

    public static OrderRespDTO.CreateOrderResultDTO toCreateOrderResultDTO(Reservation reservation) {

        return OrderRespDTO.CreateOrderResultDTO.builder()
                .reservationId(reservation.getId())
                .storeId(reservation.getStore().getId())
                .storeName(reservation.getStore().getStoreName())
                .randomBoxId(reservation.getRandomBox().getId())
                .randomBoxName(reservation.getRandomBox().getBoxName())
                .quantity(reservation.getRequestedQuantity())
                .unitPrice(reservation.getRandomBox().getPrice())
                .totalPrice(reservation.getTotalPrice())
                .reservationStatus(reservation.getStatus().name())
                .paymentStatus(reservation.getPaymentStatus() != null ? reservation.getPaymentStatus().name() : null)
                .createdAt(reservation.getCreatedAt())
                .build();
    }

    public static OrderRespDTO.CancelOrderResultDTO toCancelOrderResultDTO(Reservation reservation, String message) {
        return OrderRespDTO.CancelOrderResultDTO.builder()
                .reservationId(reservation.getId())
                .orderStatus(OrderStatusMapper.resolve(reservation).name())
                .reservationStatus(reservation.getStatus().name())
                .paymentStatus(reservation.getPaymentStatus() != null ? reservation.getPaymentStatus().name() : null)
                .message(message)
                .build();
    }

    public static OrderRespDTO.MemberOrderSummaryDTO toMemberOrderSummaryDTO(Reservation reservation) {
        return OrderRespDTO.MemberOrderSummaryDTO.builder()
                .reservationId(reservation.getId())
                .storeId(reservation.getStore().getId())
                .storeName(reservation.getStore().getStoreName())
                .randomBoxId(reservation.getRandomBox().getId())
                .randomBoxName(reservation.getRandomBox().getBoxName())
                .quantity(reservation.getRequestedQuantity())
                .totalPrice(reservation.getTotalPrice())
                .pickupTimeText(null) // 추후 픽업시간 필드 연결
                .build();
    }

    public static OrderRespDTO.MemberOrderListDTO toMemberOrderListDTO(List<Reservation> reservations) {
        List<OrderRespDTO.MemberOrderSummaryDTO> orders = reservations.stream()
                .map(OrderConverter::toMemberOrderSummaryDTO)
                .toList();

        return OrderRespDTO.MemberOrderListDTO.builder()
                .orders(orders)
                .build();
    }
}