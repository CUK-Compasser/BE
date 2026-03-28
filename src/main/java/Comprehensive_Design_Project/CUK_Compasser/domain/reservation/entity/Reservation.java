package Comprehensive_Design_Project.CUK_Compasser.domain.reservation.entity;

import Comprehensive_Design_Project.CUK_Compasser.domain.member.entity.Member;
import Comprehensive_Design_Project.CUK_Compasser.domain.order.entity.OrderStatus;
import Comprehensive_Design_Project.CUK_Compasser.domain.randomBox.entity.RandomBox;
import Comprehensive_Design_Project.CUK_Compasser.domain.store.entity.Store;
import Comprehensive_Design_Project.CUK_Compasser.global.common.BaseEntity;
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
    @JoinColumn(
            name = "member_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_resv_member")
    )
    private Member member;

    // 예약이 속한 스토어
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "store_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_resv_store")
    )
    private Store store;

    // 예약 대상 랜덤박스
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "random_box_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_resv_random_box")
    )
    private RandomBox randomBox;

    // 예약 상태
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

    @Column(name = "deposit_confirmed_at")
    private LocalDateTime depositConfirmedAt;

    @Column(name = "picked_up_at")
    private LocalDateTime pickedUpAt;

    // 결제 상태
    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", nullable = false, length = 30)
    private PaymentStatus paymentStatus = PaymentStatus.PENDING;

    // 픽업 상태
    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "pickup_status", nullable = false, length = 30)
    private PickupStatus pickupStatus = PickupStatus.WAITING;

    // 거절 사유
    @Column(name = "reject_reason", length = 500)
    private String rejectReason;

    @Column(name = "payment_tid", length = 100)
    private String paymentTid;

    @Column(name = "payment_method", length = 30)
    private String paymentMethod;

    @Column(name = "paid_at")
    private LocalDateTime paidAt;

    public void updatePaymentTid(String paymentTid) {
        this.paymentTid = paymentTid;
    }

    public void markPaid(String paymentMethod) {
        this.paymentMethod = paymentMethod;
        this.paidAt = LocalDateTime.now();
        this.status = OrderStatus.PAID;
    }

    @PrePersist
    protected void prePersist() {
        if (this.status == null) {
            this.status = ReservationStatus.REQUESTED;
        }
        if (this.paymentStatus == null) {
            this.paymentStatus = PaymentStatus.PENDING;
        }
        if (this.pickupStatus == null) {
            this.pickupStatus = PickupStatus.WAITING;
        }
    }

    public void approve() {
        this.status = ReservationStatus.APPROVED;
        this.rejectReason = null;
    }

    public void reject(String rejectReason) {
        this.status = ReservationStatus.REJECTED;
        this.rejectReason = rejectReason;
        this.pickupStatus = PickupStatus.WAITING;
    }

    public void confirmDeposit() {
        this.paymentStatus = PaymentStatus.PAID;
    }

    public void markPreparing() {
        this.pickupStatus = PickupStatus.PREPARING;
    }

    public void markReady() {
        this.pickupStatus = PickupStatus.READY;
    }

    public void markPickedUp() {
        this.pickupStatus = PickupStatus.PICKED_UP;
    }

    public void cancel(String reason) {
        this.status = ReservationStatus.CANCELED;
        this.rejectReason = reason;
    }
}