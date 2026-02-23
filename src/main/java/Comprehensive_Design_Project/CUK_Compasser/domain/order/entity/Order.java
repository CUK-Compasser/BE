package Comprehensive_Design_Project.CUK_Compasser.domain.order.entity;


import Comprehensive_Design_Project.CUK_Compasser.domain.member.entity.Member;
import Comprehensive_Design_Project.CUK_Compasser.domain.orderHistory.entity.OrderHistory;
import Comprehensive_Design_Project.CUK_Compasser.domain.store.entity.Store;
import Comprehensive_Design_Project.CUK_Compasser.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "orders",
        indexes = {
                @Index(name = "idx_orders_member", columnList = "member_id"),
                @Index(name = "idx_orders_store", columnList = "store_id"),
                @Index(name = "idx_orders_status", columnList = "status")
        }
)
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Order extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id", nullable = false, foreignKey = @ForeignKey(name = "fk_orders_member"))
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "store_id", nullable = false, foreignKey = @ForeignKey(name = "fk_orders_store"))
    private Store store;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private OrderStatus status;

    @Column(name = "total_price", nullable = false)
    private Integer totalPrice;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<OrderHistory> histories = new ArrayList<>();

    // 상태 변경 시 이력 남기는 편의 메서드(선택)
    public void changeStatus(OrderStatus to, String memo) {
        OrderStatus from = this.status;
        this.status = to;
        this.histories.add(OrderHistory.builder()
                .order(this)
                .fromStatus(from)
                .toStatus(to)
                .memo(memo)
                .build());
    }
}