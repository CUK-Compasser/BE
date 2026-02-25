package Comprehensive_Design_Project.CUK_Compasser.domain.store.entity;

import Comprehensive_Design_Project.CUK_Compasser.domain.randomBox.entity.RandomBox;
import Comprehensive_Design_Project.CUK_Compasser.domain.storeManager.entity.StoreManager;
import Comprehensive_Design_Project.CUK_Compasser.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "stores",
        indexes = {
                @Index(name = "idx_stores_manager", columnList = "store_manager_id"),
                @Index(name = "idx_stores_geo", columnList = "latitude,longitude")
        }
)
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Store extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "store_manager_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_stores_manager")
    )
    private StoreManager storeManager;

    @Column(name = "store_name", nullable = false, length = 100)
    private String storeName;

    @Column(name = "store_details", length = 500)
    private String storeDetails;

    @Column(name = "before_price")
    private Integer beforePrice;

    @Column(name = "after_price")
    private Integer afterPrice;

    @Column(precision = 10, scale = 7)
    private BigDecimal latitude;

    @Column(precision = 10, scale = 7)
    private BigDecimal longitude;

    /** JSON 컬럼 - 우선 String으로 (원하면 JsonNode + Converter로 개선 가능) */
    @Column(name = "business_hours", columnDefinition = "json")
    private String businessHours;

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<StoreImage> images = new ArrayList<>();

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<RandomBox> randomBoxes = new ArrayList<>();
}