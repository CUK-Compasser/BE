package Comprehensive_Design_Project.CUK_Compasser.domain.randomBox.entity;

import Comprehensive_Design_Project.CUK_Compasser.domain.store.entity.Store;
import Comprehensive_Design_Project.CUK_Compasser.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "random_boxes",
        indexes = {
                @Index(name = "idx_random_boxes_store", columnList = "store_id"),
                @Index(name = "idx_random_boxes_status", columnList = "sale_status")
        }
)
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class RandomBox extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "store_id", nullable = false, foreignKey = @ForeignKey(name = "fk_random_boxes_store"))
    private Store store;

    @Column(name = "box_name", nullable = false, length = 100)
    private String boxName;

    @Column(length = 1000)
    private String content;

    @Column(nullable = false)
    private Integer stock;

    @Column(name = "before_price", nullable = false)
    private Integer beforePrice;

    @Column(name = "after_price", nullable = false)
    private Integer afterPrice;

    @Enumerated(EnumType.STRING)
    @Column(name = "sale_status", nullable = false, length = 30)
    private SaleStatus saleStatus;
}