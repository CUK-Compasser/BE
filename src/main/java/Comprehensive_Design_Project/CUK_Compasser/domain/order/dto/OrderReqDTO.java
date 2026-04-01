package Comprehensive_Design_Project.CUK_Compasser.domain.order.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class OrderReqDTO {

    @Getter
    public static class CreateOrderDTO {

        @NotNull
        private Long randomBoxId;

        @NotNull
        @Min(1)
        private Integer quantity;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CancelOrderResultDTO {
        private Long reservationId;

        // 사용자 화면에 보여줄 대표 주문 상태
        private String orderStatus;

        // 실제 DB 상태값
        private String reservationStatus;
        private String paymentStatus;

        private String message;
    }

}