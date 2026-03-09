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
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_stores_store_manager", columnNames = {"store_manager_id"})
        },
        indexes = {
                @Index(name = "idx_stores_manager", columnList = "store_manager_id"),
                @Index(name = "idx_stores_geo", columnList = "latitude,longitude")
        }
)
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Store extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "store_manager_id",
            nullable = false,
            unique = true,
            foreignKey = @ForeignKey(name = "fk_stores_manager")
    )
    private StoreManager storeManager;

    @Column(name = "store_name", nullable = false, length = 100)
    private String storeName;

    @Column(name = "store_email", length = 255)
    private String storeEmail;

    @Enumerated(EnumType.STRING)
    private Tag tag;

    @Column(name = "store_details", length = 500)
    private String storeDetails;

    @Column(name = "input_address", length = 255)
    private String inputAddress;

    @Column(name = "road_address", length = 255)
    private String roadAddress;

    @Column(name = "jibun_address", length = 255)
    private String jibunAddress;

    @Column(precision = 10, scale = 7)
    private BigDecimal latitude;

    @Column(precision = 10, scale = 7)
    private BigDecimal longitude;

    @Column(name = "business_hours", columnDefinition = "json")
    private String businessHours;

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<StoreImage> images = new ArrayList<>();

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<RandomBox> randomBoxes = new ArrayList<>();
}