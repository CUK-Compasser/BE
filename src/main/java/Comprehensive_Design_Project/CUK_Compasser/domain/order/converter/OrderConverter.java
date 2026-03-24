package Comprehensive_Design_Project.CUK_Compasser.domain.order.converter;

import Comprehensive_Design_Project.CUK_Compasser.domain.order.dto.OrderRespDTO;
import Comprehensive_Design_Project.CUK_Compasser.domain.reservation.entity.PaymentStatus;
import Comprehensive_Design_Project.CUK_Compasser.domain.reservation.entity.PickupStatus;
import Comprehensive_Design_Project.CUK_Compasser.domain.reservation.entity.Reservation;
import Comprehensive_Design_Project.CUK_Compasser.domain.reservation.entity.ReservationStatus;

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
                .orderStatus(resolveOrderStatus(reservation))
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
                .orderStatus(resolveOrderStatus(reservation))
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
                .orderStatus(resolveOrderStatus(reservation))
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

    private static String resolveOrderStatus(Reservation reservation) {
        if (reservation.getStatus() == ReservationStatus.REJECTED) {
            return "REJECTED";
        }

        if (reservation.getStatus() == ReservationStatus.CANCELED) {
            return "CANCELED";
        }

        if (reservation.getPickupStatus() == PickupStatus.PICKED_UP) {
            return "PICKED_UP";
        }

        if (reservation.getPickupStatus() == PickupStatus.READY) {
            return "READY";
        }

        if (reservation.getPickupStatus() == PickupStatus.PREPARING) {
            return "PREPARING";
        }

        if (reservation.getPaymentStatus() == PaymentStatus.PAID) {
            return "PAID";
        }

        if (reservation.getStatus() == ReservationStatus.APPROVED) {
            return "APPROVED";
        }

        return "REQUESTED";
    }
}