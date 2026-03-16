package Comprehensive_Design_Project.CUK_Compasser.domain.order.repository;

import Comprehensive_Design_Project.CUK_Compasser.domain.order.entity.Order;
import Comprehensive_Design_Project.CUK_Compasser.domain.order.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Long countByMember_IdAndStatus(Long memberId, OrderStatus status);
}
