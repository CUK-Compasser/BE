package Comprehensive_Design_Project.CUK_Compasser.domain.storeManager.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class StoreManager {
    @Id @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;
}
