package Comprehensive_Design_Project.CUK_Compasser.domain.reservation.dto;

import Comprehensive_Design_Project.CUK_Compasser.domain.reservation.entity.ReservationStatus;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

public class ReservationRespDTO {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReservationSummaryDTO {
        private Long reservationId;
        private Long memberId;
        private String memberName;
        private Long storeId;
        private String storeName;
        private ReservationStatus status;
        private Integer requestedQuantity;
        private Integer approvedQuantity;
        private String memo;
        private String rejectReason;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReservationDetailDTO {
        private Long reservationId;
        private Long memberId;
        private String memberName;
        private Long storeId;
        private String storeName;
        private ReservationStatus status;
        private Integer requestedQuantity;
        private Integer approvedQuantity;
        private String memo;
        private String rejectReason;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReservationListDTO {
        private List<ReservationSummaryDTO> reservations;
        private Integer count;
    }
}