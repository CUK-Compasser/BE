package Comprehensive_Design_Project.CUK_Compasser.domain.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class OrderRespDTO {

    /**
     * 사용자 주문 생성 응답
     * - 주문 직후 사용자에게 보여줄 기본 정보
     */
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

        private LocalDateTime createdAt;
    }

    /**
     * 사용자 주문 목록 카드 1개
     * - 사용자 주문 확인 화면에 필요한 정보만 담는다.
     */
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MemberOrderSummaryDTO {
        private Long reservationId;
        private Long storeId;
        private String storeName;

        private Long randomBoxId;
        private String randomBoxName;

        private Integer quantity;
        private Integer totalPrice;

        private String pickupTimeText;
    }

    /**
     * 사용자 주문 목록 응답
     */
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MemberOrderListDTO {
        private List<MemberOrderSummaryDTO> orders;
    }

    /**
     * 사용자 주문 상세/상태 조회 응답
     * - 목록보다 조금 더 자세한 정보가 필요할 때 사용
     */
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MemberOrderDetailDTO {
        private Long reservationId;
        private Long storeId;
        private String storeName;

        private Long randomBoxId;
        private String randomBoxName;

        private Integer quantity;
        private Integer totalPrice;

        private String orderStatus;
        private String reservationStatus;
        private String paymentStatus;

        private String pickupTimeText;
        private String businessHours;

        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CompleteOrderResultDTO {
        private Long reservationId;
        private String orderStatus;
        private String reservationStatus;
        private String paymentStatus;
        private String message;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CancelOrderResultDTO {
        private Long reservationId;
        private String orderStatus;
        private String reservationStatus;
        private String paymentStatus;
        private String message;
    }
}