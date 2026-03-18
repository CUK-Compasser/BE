package Comprehensive_Design_Project.CUK_Compasser.domain.order.service;

import Comprehensive_Design_Project.CUK_Compasser.domain.order.dto.OrderReqDTO;
import Comprehensive_Design_Project.CUK_Compasser.domain.order.dto.OrderRespDTO;

public interface OrderService {

    OrderRespDTO.CreateOrderResultDTO createOrder(Long memberId, OrderReqDTO.CreateOrderDTO request);

    OrderRespDTO.CompleteOrderResultDTO completeOrder(Long memberId, Long reservationId, OrderReqDTO.CompleteOrderDTO request);

    OrderRespDTO.OrderStatusDTO getOrderStatus(Long memberId, Long reservationId);
}