package Comprehensive_Design_Project.CUK_Compasser.domain.reservation.entity;

import Comprehensive_Design_Project.CUK_Compasser.domain.member.entity.Member;
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
                @Index(name = "idx_resv_store_status", columnList = "store_id,status")
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

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id", nullable = false, foreignKey = @ForeignKey(name = "fk_resv_member"))
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "store_id", nullable = false, foreignKey = @ForeignKey(name = "fk_resv_store"))
    private Store store;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private ReservationStatus status = ReservationStatus.REQUESTED;

    @Column(name = "requested_quantity", nullable = false)
    private Integer requestedQuantity;

    @Column(name = "approved_quantity")
    private Integer approvedQuantity;

    @Column(length = 500)
    private String memo;

    @Column(name = "reject_reason", length = 500)
    private String rejectReason;

    @PrePersist
    protected void prePersist() {
        if (this.status == null) {
            this.status = ReservationStatus.REQUESTED;
        }
    }
}