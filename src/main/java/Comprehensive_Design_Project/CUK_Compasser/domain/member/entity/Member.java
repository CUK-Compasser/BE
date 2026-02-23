package Comprehensive_Design_Project.CUK_Compasser.domain.member.entity;

import Comprehensive_Design_Project.CUK_Compasser.domain.order.entity.Order;
import Comprehensive_Design_Project.CUK_Compasser.domain.store.entity.Store;
import Comprehensive_Design_Project.CUK_Compasser.domain.storeManager.entity.StoreManager;
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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "member")
    private StoreManager storeManager;

    @OneToMany
    private List<Order> orders;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Login login;


    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String memberName;


}
