package Comprehensive_Design_Project.CUK_Compasser.domain.store.entity;


import Comprehensive_Design_Project.CUK_Compasser.domain.storeManager.entity.StoreManager;
import jakarta.persistence.*;

@Entity
public class Store {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "store_manager_id")
    private StoreManager storeManager;

}
