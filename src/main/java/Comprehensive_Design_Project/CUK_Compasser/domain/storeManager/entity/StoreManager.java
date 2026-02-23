package Comprehensive_Design_Project.CUK_Compasser.domain.storeManager.entity;

import Comprehensive_Design_Project.CUK_Compasser.domain.member.entity.Member;
import Comprehensive_Design_Project.CUK_Compasser.domain.store.entity.Store;
import jakarta.persistence.*;

@Entity
public class StoreManager {
    @Id @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Member member;

    @OneToOne
    private Store store;
}
