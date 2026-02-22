package Comprehensive_Design_Project.CUK_Compasser.domain.order.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "orders") // order가 예약어
public class Order {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
