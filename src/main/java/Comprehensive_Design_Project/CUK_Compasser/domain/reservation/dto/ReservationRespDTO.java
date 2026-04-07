package Comprehensive_Design_Project.CUK_Compasser.domain.reservation.dto;

import Comprehensive_Design_Project.CUK_Compasser.domain.reservation.entity.ReservationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class ReservationRespDTO {

    /**
     * 사장님 예약/주문 관리 화면에서 사용하는 카드 DTO
     * - 주문자, 매장, 랜덤박스, 가격, 수량, 상태 정보를 담는다.
     */
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReservationDTO {
        private Long reservationId;

        private Long memberId;
        private String customerName;

        private Long storeId;
        private String storeName;

        private Long randomBoxId;
        private String randomBoxName;
        private Integer totalPrice;

        private ReservationStatus status;
        private Integer requestedQuantity;
        private String rejectReason;
    }

    /**
     * 사장님 예약/주문 리스트 응답 DTO
     */
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReservationListDTO {
        private List<ReservationDTO> reservations;
    }
}