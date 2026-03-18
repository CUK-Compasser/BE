package Comprehensive_Design_Project.CUK_Compasser.domain.order.converter;

import Comprehensive_Design_Project.CUK_Compasser.domain.order.dto.OrderRespDTO;
import Comprehensive_Design_Project.CUK_Compasser.domain.reservation.entity.Reservation;

public class OrderConverter {

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
                .paymentStatus(reservation.getPaymentStatus().name())
                .pickupStatus(reservation.getPickupStatus().name())
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
                .memberBankType(reservation.getMember().getBankType())
                .reservationStatus(reservation.getStatus().name())
                .paymentStatus(reservation.getPaymentStatus().name())
                .pickupStatus(reservation.getPickupStatus().name())
                .message(message)
                .build();
    }

    public static OrderRespDTO.OrderStatusDTO toOrderStatusDTO(Reservation reservation) {
        return OrderRespDTO.OrderStatusDTO.builder()
                .reservationId(reservation.getId())
                .storeId(reservation.getStore().getId())
                .storeName(reservation.getStore().getStoreName())
                .randomBoxId(reservation.getRandomBox().getId())
                .randomBoxName(reservation.getRandomBox().getBoxName())
                .quantity(reservation.getRequestedQuantity())
                .totalPrice(reservation.getTotalPrice())
                .reservationStatus(reservation.getStatus().name())
                .paymentStatus(reservation.getPaymentStatus().name())
                .pickupStatus(reservation.getPickupStatus().name())
                .memberBankType(reservation.getMember().getBankType())
                .depositBankType(reservation.getStore().getStoreManager().getDepositBankType())
                .depositAccountNumber(reservation.getStore().getStoreManager().getDepositAccountNumber())
                .depositAccountHolder(reservation.getStore().getStoreManager().getDepositAccountHolder())
                .businessHours(reservation.getStore().getBusinessHours())
                .createdAt(reservation.getCreatedAt())
                .updatedAt(reservation.getUpdatedAt())
                .build();
    }
}