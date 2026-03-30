package Comprehensive_Design_Project.CUK_Compasser.domain.reservation.entity;

public enum PickupStatus {
    //픽업 수령/준비 흐 상태
    /**
     * 사장님 상품 준비 및 픽업 상태
     * 점주 운영 API에서 사용
     */
    WAITING,
    PREPARING,
    READY,
    PICKED_UP
}