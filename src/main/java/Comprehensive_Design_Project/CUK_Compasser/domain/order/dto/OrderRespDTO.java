package Comprehensive_Design_Project.CUK_Compasser.domain.order.dto;

import Comprehensive_Design_Project.CUK_Compasser.domain.member.entity.BankType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class OrderRespDTO {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateOrderResultDTO {
        private Long reservationId;
        private Long storeId;
        private String storeName;

        private Long randomBoxId;
        private String randomBoxName;

        private Integer quantity;
        private Integer unitPrice;
        private Integer totalPrice;

        private String reservationStatus;
        private String paymentStatus;
        private String pickupStatus;

        private BankType memberBankType;

        private BankType depositBankType;
        private String depositAccountNumber;
        private String depositAccountHolder;

        private String businessHours;
        private LocalDateTime createdAt;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CompleteOrderResultDTO {
        private Long reservationId;
        private BankType memberBankType;
        private String reservationStatus;
        private String paymentStatus;
        private String pickupStatus;
        private String message;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderStatusDTO {
        private Long reservationId;
        private Long storeId;
        private String storeName;

        private Long randomBoxId;
        private String randomBoxName;

        private Integer quantity;
        private Integer totalPrice;

        private String reservationStatus;
        private String paymentStatus;
        private String pickupStatus;

        private BankType memberBankType;

        private BankType depositBankType;
        private String depositAccountNumber;
        private String depositAccountHolder;

        private String businessHours;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }
}