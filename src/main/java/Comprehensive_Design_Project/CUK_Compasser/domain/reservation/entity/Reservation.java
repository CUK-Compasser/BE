package Comprehensive_Design_Project.CUK_Compasser.domain.reservation.entity;

import Comprehensive_Design_Project.CUK_Compasser.domain.member.entity.Member;
import Comprehensive_Design_Project.CUK_Compasser.domain.randomBox.entity.RandomBox;
import Comprehensive_Design_Project.CUK_Compasser.domain.store.entity.Store;
import Comprehensive_Design_Project.CUK_Compasser.global.common.BaseEntity;
import Comprehensive_Design_Project.CUK_Compasser.global.common.apiPayload.code.status.ErrorStatus;
import Comprehensive_Design_Project.CUK_Compasser.global.common.apiPayload.exception.GeneralException;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "reservations",
        indexes = {
                @Index(name = "idx_resv_member", columnList = "member_id"),
                @Index(name = "idx_resv_store", columnList = "store_id"),
                @Index(name = "idx_resv_random_box", columnList = "random_box_id"),
                @Index(name = "idx_resv_store_status", columnList = "store_id,status"),
                @Index(name = "idx_resv_random_box_status", columnList = "random_box_id,status")
        }
)
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Reservation extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 예약한 사용자
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_resv_member"))
    private Member member;

    // 예약이 속한 스토어
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "store_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_resv_store"))
    private Store store;

    // 예약 대상 랜덤박스
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "random_box_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_resv_random_box"))
    private RandomBox randomBox;

    // 예약 상태: 요청/승인/거절/취소
    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private ReservationStatus status = ReservationStatus.REQUESTED;

    // 사용자가 요청한 수량
    @Column(name = "requested_quantity", nullable = false)
    private Integer requestedQuantity;

    // 총 결제 금액
    @Column(name = "total_price", nullable = false)
    private Integer totalPrice;

    // 픽업 완료 시각
    @Column(name = "picked_up_at")
    private LocalDateTime pickedUpAt;

    // 결제 상태
    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", nullable = false, length = 30)
    private PaymentStatus paymentStatus = PaymentStatus.PENDING;


    // 거절/취소 사유
    @Column(name = "reject_reason", length = 500)
    private String rejectReason;

    // 카카오페이 ready 응답의 tid
    @Column(name = "payment_tid", length = 100)
    private String paymentTid;

    // 결제 수단
    @Column(name = "payment_method", length = 30)
    private String paymentMethod;

    // 실제 결제 완료 시각
    @Column(name = "paid_at")
    private LocalDateTime paidAt;

    @PrePersist
    protected void prePersist() {
        if (this.status == null) {
            this.status = ReservationStatus.REQUESTED;
        }
        if (this.paymentStatus == null) {
            this.paymentStatus = PaymentStatus.PENDING;
        }
    }

    /**
     * 카카오페이 결제 준비 완료
     * - ready API 응답으로 받은 tid 저장
     * - 결제 진행 가능 상태로 변경
     */
    public void markPaymentReady(String paymentTid, String paymentMethod) {
        this.paymentTid = paymentTid;
        this.paymentMethod = paymentMethod;
        this.paymentStatus = PaymentStatus.READY;
    }

    /**
     * 카카오페이 결제 승인 완료
     */
    public void markPaid(String paymentMethod) {
        this.paymentMethod = paymentMethod;
        this.paymentStatus = PaymentStatus.PAID;
        this.paidAt = LocalDateTime.now();
    }

    /**
     * 결제 실패 처리
     * - 예약 자체는 유지하고 결제 상태만 FAILED 처리
     */
    public void markPaymentFailed() {
        this.paymentStatus = PaymentStatus.FAILED;
    }

    /**
     * 결제 취소 처리
     * - 예약 자체는 유지하고 결제 상태만 CANCELED 처리
     */
    public void cancelPayment() {
        this.paymentStatus = PaymentStatus.CANCELED;
    }

    public void approve() {
        this.status = ReservationStatus.APPROVED;
        this.rejectReason = null;
    }

    public void reject(String rejectReason) {
        this.status = ReservationStatus.REJECTED;
        this.rejectReason = rejectReason;
    }

    public void markRefunded() {
        if (this.paymentStatus != PaymentStatus.PAID) {
            throw new GeneralException(ErrorStatus.INVALID_PAYMENT_STATUS);
        }
        this.paymentStatus = PaymentStatus.REFUNDED;
    }
}