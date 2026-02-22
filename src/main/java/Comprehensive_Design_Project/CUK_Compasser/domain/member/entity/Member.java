package Comprehensive_Design_Project.CUK_Compasser.domain.member.entity;

import Comprehensive_Design_Project.CUK_Compasser.domain.order.entity.Order;
import Comprehensive_Design_Project.CUK_Compasser.domain.store.entity.Store;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Store store;

    @OneToMany
    private List<Order> orders;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Login login;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String memberName;

}
