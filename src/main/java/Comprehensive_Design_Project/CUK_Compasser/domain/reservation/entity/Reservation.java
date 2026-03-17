package Comprehensive_Design_Project.CUK_Compasser.domain.reservation.entity;

import Comprehensive_Design_Project.CUK_Compasser.domain.member.entity.Member;
import Comprehensive_Design_Project.CUK_Compasser.domain.randomBox.entity.RandomBox;
import Comprehensive_Design_Project.CUK_Compasser.domain.store.entity.Store;
import Comprehensive_Design_Project.CUK_Compasser.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

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
    // 일단 기존 구조 호환 때문에 유지
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

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private ReservationStatus status = ReservationStatus.REQUESTED;

    // 사용자가 요청한 수량
    @Column(name = "requested_quantity", nullable = false)
    private Integer requestedQuantity;

    // 거절 사유
    @Column(name = "reject_reason", length = 500)
    private String rejectReason;

    @PrePersist
    protected void prePersist() {
        if (this.status == null) {
            this.status = ReservationStatus.REQUESTED;
        }
    }
}