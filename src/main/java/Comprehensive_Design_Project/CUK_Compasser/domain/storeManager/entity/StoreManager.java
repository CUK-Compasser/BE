package Comprehensive_Design_Project.CUK_Compasser.domain.storeManager.entity;

import Comprehensive_Design_Project.CUK_Compasser.domain.member.entity.Member;
import Comprehensive_Design_Project.CUK_Compasser.domain.store.entity.Store;
import Comprehensive_Design_Project.CUK_Compasser.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "store_managers",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_store_managers_biz_no", columnNames = {"business_license_number"})
        }
)
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class StoreManager extends BaseEntity {

    @Id
    @Column(name = "member_id")
    private Long id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id", foreignKey = @ForeignKey(name = "fk_store_managers_member"))
    private Member member;

    @Column(name = "business_license_number", nullable = false, length = 30)
    private String businessLicenseNumber;

    @Column(name = "verified_at")
    private LocalDateTime verifiedAt;

    @OneToOne(mappedBy = "storeManager", fetch = FetchType.LAZY)
    private Store store;
}