package Comprehensive_Design_Project.CUK_Compasser.domain.order.converter;

import Comprehensive_Design_Project.CUK_Compasser.domain.order.dto.OrderRespDTO;
import Comprehensive_Design_Project.CUK_Compasser.domain.order.entity.OrderStatus;
import Comprehensive_Design_Project.CUK_Compasser.domain.reservation.entity.Reservation;

public class OrderConverter {

    private OrderConverter() {
    }

    public static OrderRespDTO.CreateOrderResultDTO toCreateOrderResultDTO(Reservation reservation) {
        OrderStatus orderStatus = OrderStatusMapper.resolve(reservation);

        return OrderRespDTO.CreateOrderResultDTO.builder()
                .reservationId(reservation.getId())
                .storeId(reservation.getStore().getId())
                .storeName(reservation.getStore().getStoreName())
                .randomBoxId(reservation.getRandomBox().getId())
                .randomBoxName(reservation.getRandomBox().getBoxName())
                .quantity(reservation.getRequestedQuantity())
                .unitPrice(reservation.getRandomBox().getPrice())
                .totalPrice(reservation.getTotalPrice())
                .orderStatus(orderStatus.name())
                .reservationStatus(reservation.getStatus().name())
                .paymentStatus(reservation.getPaymentStatus() != null ? reservation.getPaymentStatus().name() : null)
                .pickupStatus(reservation.getPickupStatus() != null ? reservation.getPickupStatus().name() : null)
                .memberBankType(reservation.getMember().getBankType())
                .depositBankType(reservation.getStore().getStoreManager().getDepositBankType())
                .depositAccountNumber(reservation.getStore().getStoreManager().getDepositAccountNumber())
                .depositAccountHolder(reservation.getStore().getStoreManager().getDepositAccountHolder())
                .businessHours(reservation.getStore().getBusinessHours())
                .createdAt(reservation.getCreatedAt())
                .build();
    }

    public static OrderRespDTO.CompleteOrderResultDTO toCompleteOrderResultDTO(Reservation reservation, String message) {
        return OrderRespDTO.CompleteOrderResultDTO.builder()
                .reservationId(reservation.getId())
                .orderStatus(OrderStatusMapper.resolve(reservation).name())
                .reservationStatus(reservation.getStatus().name())
                .paymentStatus(reservation.getPaymentStatus() != null ? reservation.getPaymentStatus().name() : null)
                .pickupStatus(reservation.getPickupStatus() != null ? reservation.getPickupStatus().name() : null)
                .message(message)
                .build();
    }

    public static OrderRespDTO.OrderStatusDTO toOrderStatusDTO(Reservation reservation) {
        OrderStatus orderStatus = OrderStatusMapper.resolve(reservation);

        return OrderRespDTO.OrderStatusDTO.builder()
                .reservationId(reservation.getId())
                .storeId(reservation.getStore().getId())
                .storeName(reservation.getStore().getStoreName())
                .randomBoxId(reservation.getRandomBox().getId())
                .randomBoxName(reservation.getRandomBox().getBoxName())
                .quantity(reservation.getRequestedQuantity())
                .totalPrice(reservation.getTotalPrice())
                .orderStatus(orderStatus.name())
                .reservationStatus(reservation.getStatus().name())
                .paymentStatus(reservation.getPaymentStatus() != null ? reservation.getPaymentStatus().name() : null)
                .pickupStatus(reservation.getPickupStatus() != null ? reservation.getPickupStatus().name() : null)
                .memberBankType(reservation.getMember().getBankType())
                .depositBankType(reservation.getStore().getStoreManager().getDepositBankType())
                .depositAccountNumber(reservation.getStore().getStoreManager().getDepositAccountNumber())
                .depositAccountHolder(reservation.getStore().getStoreManager().getDepositAccountHolder())
                .businessHours(reservation.getStore().getBusinessHours())
                .createdAt(reservation.getCreatedAt())
                .updatedAt(reservation.getUpdatedAt())
                .build();
    }

    public static OrderRespDTO.CancelOrderResultDTO toCancelOrderResultDTO(Reservation reservation, String message) {
        return OrderRespDTO.CancelOrderResultDTO.builder()
                .reservationId(reservation.getId())
                .orderStatus(OrderStatusMapper.resolve(reservation).name())
                .reservationStatus(reservation.getStatus().name())
                .paymentStatus(reservation.getPaymentStatus() != null ? reservation.getPaymentStatus().name() : null)
                .pickupStatus(reservation.getPickupStatus() != null ? reservation.getPickupStatus().name() : null)
                .message(message)
                .build();
    }
}