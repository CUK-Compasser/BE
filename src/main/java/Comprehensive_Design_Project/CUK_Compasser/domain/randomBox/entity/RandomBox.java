package Comprehensive_Design_Project.CUK_Compasser.domain.randomBox.entity;

import Comprehensive_Design_Project.CUK_Compasser.domain.store.entity.Store;
import Comprehensive_Design_Project.CUK_Compasser.global.common.BaseEntity;
import Comprehensive_Design_Project.CUK_Compasser.global.common.apiPayload.code.status.ErrorStatus;
import Comprehensive_Design_Project.CUK_Compasser.global.common.apiPayload.exception.GeneralException;
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
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class RandomBox extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "store_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_random_boxes_store")
    )
    private Store store;

    @Column(name = "box_name", nullable = false, length = 100)
    private String boxName;

    @Column(length = 1000)
    private String content;

    @Column(nullable = false)
    private Integer stock;

    @Column(name = "price", nullable = false)
    private Integer price;

    @Column(name = "buy_limit", nullable = false)
    private Integer buyLimit;

    @Column(name = "pickup_time_info", columnDefinition = "TEXT", nullable = false)
    private String pickupTimeInfo;

    @Enumerated(EnumType.STRING)
    @Column(name = "sale_status", nullable = false, length = 30)
    private SaleStatus saleStatus;

    public void decreaseStock(int quantity) {
        if (quantity <= 0) {
            throw new GeneralException(ErrorStatus.INVALID_ORDER_QUANTITY);
        }

        if (this.saleStatus != SaleStatus.READY) {
            throw new GeneralException(ErrorStatus.RANDOM_BOX_NOT_ON_SALE);
        }

        if (this.stock < quantity) {
            throw new GeneralException(ErrorStatus.RANDOM_BOX_STOCK_NOT_ENOUGH);
        }

        this.stock -= quantity;

        if (this.stock == 0) {
            this.saleStatus = SaleStatus.SOLD_OUT;
        }
    }

    public void increaseStock(int quantity) {
        if (quantity <= 0) {
            return;
        }

        this.stock += quantity;

        if (this.stock > 0 && this.saleStatus == SaleStatus.SOLD_OUT) {
            this.saleStatus = SaleStatus.READY;
        }
    }
}