package Comprehensive_Design_Project.CUK_Compasser.domain.university.domain;

import Comprehensive_Design_Project.CUK_Compasser.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(
        name = "universities",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_universities_name_campus", columnNames = {"name", "campus_name"})
        },
        indexes = {
                @Index(name = "idx_universities_geo", columnList = "latitude,longitude"),
                @Index(name = "idx_universities_active", columnList = "is_active,sort_order")
        }
)
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