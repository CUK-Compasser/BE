package Comprehensive_Design_Project.CUK_Compasser.domain.randomBox.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class RandomBox {
    @Id @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;
}
