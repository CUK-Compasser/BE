package Comprehensive_Design_Project.CUK_Compasser.domain.settlement.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

public class SettlementRespDTO {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SettlementPreviewDTO {
        private Long storeId;
        private String storeName;
        private Integer count;
        private Integer totalAmount;
        private List<SettlementReservationDTO> reservations;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SettlementReservationDTO {
        private Long reservationId;
        private Long memberId;
        private Integer totalPrice;
        private LocalDateTime createdAt;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SettlementCompleteDTO {
        private Long storeId;
        private String storeName;
        private Integer count;
        private Integer totalAmount;
        private String message;
    }
}