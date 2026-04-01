package Comprehensive_Design_Project.CUK_Compasser.domain.reservation.entity;

public enum PaymentStatus {
    PENDING,   // 결제 전
    READY,     // 카카오페이 ready 완료, 결제창 진입 가능
    PAID,      // 승인 완료
    FAILED,    // 결제 실패
    CANCELED,   // 결제 취소
    REFUNDED    // 환불
}