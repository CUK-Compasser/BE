package Comprehensive_Design_Project.CUK_Compasser.domain.university.domain;

import Comprehensive_Design_Project.CUK_Compasser.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

/*@Entity
@Table(
        name = "universities",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_universities_name_campus", columnNames = {"name", "campus_name"})
        },
        indexes = {
                @Index(name = "idx_universities_geo", columnList = "latitude,longitude"),
                @Index(name = "idx_universities_active", columnList = "is_active,sort_order")
        }
)*/

// 특정 대학교도 FE에서 정확한 주소로 반환 후 받을 예정이라 엔티티 필요가 없을 것 같아서 주석 처리했습니다.
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class University extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String name;

    @Column(name = "campus_name", length = 120)
    private String campusName;

    @Column(length = 255)
    private String address;

    @Column(nullable = false, precision = 10, scale = 7)
    private BigDecimal latitude;

    @Column(nullable = false, precision = 10, scale = 7)
    private BigDecimal longitude;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder;
}