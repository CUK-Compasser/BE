package Comprehensive_Design_Project.CUK_Compasser.domain.orderHistory.entity;

import Comprehensive_Design_Project.CUK_Compasser.domain.order.entity.Order;
import Comprehensive_Design_Project.CUK_Compasser.domain.order.entity.OrderStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "order_histories",
        indexes = {
                @Index(name = "idx_order_hist_order", columnList = "order_id"),
                @Index(name = "idx_order_hist_changed", columnList = "changed_at")
        }
)
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class OrderHistory {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id", nullable = false, foreignKey = @ForeignKey(name = "fk_order_hist_order"))
    private Order order;

    @Enumerated(EnumType.STRING)
    @Column(name = "from_status", length = 30)
    private OrderStatus fromStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "to_status", nullable = false, length = 30)
    private OrderStatus toStatus;

    @Column(name = "changed_at", nullable = false, updatable = false)
    private LocalDateTime changedAt;

    @Column(length = 500)
    private String memo;

    @PrePersist
    void prePersist() {
        this.changedAt = LocalDateTime.now();
    }
}