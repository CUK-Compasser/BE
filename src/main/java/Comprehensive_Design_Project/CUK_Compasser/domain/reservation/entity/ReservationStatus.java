package Comprehensive_Design_Project.CUK_Compasser.domain.reservation.entity;

public enum ReservationStatus {
    /**
     * 주문 요청에 대한 승인/거절/취소 상태
     * 사장님 승인/거절 처리와 사용자 취소 처리에 사용
     */
    //점주가 예약 요청을 처리하는 승인 흐름
    REQUESTED, APPROVED, REJECTED, CANCELED
}