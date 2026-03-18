package Comprehensive_Design_Project.CUK_Compasser.domain.order.controller;

import Comprehensive_Design_Project.CUK_Compasser.domain.order.dto.OrderReqDTO;
import Comprehensive_Design_Project.CUK_Compasser.domain.order.dto.OrderRespDTO;
import Comprehensive_Design_Project.CUK_Compasser.domain.order.service.OrderService;
import Comprehensive_Design_Project.CUK_Compasser.global.common.apiPayload.ApiResponse;
import Comprehensive_Design_Project.CUK_Compasser.global.common.apiPayload.code.status.SuccessStatus;
import Comprehensive_Design_Project.CUK_Compasser.global.security.userDetails.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ApiResponse<OrderRespDTO.CreateOrderResultDTO> createOrder(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody @Valid OrderReqDTO.CreateOrderDTO request
    ) {
        OrderRespDTO.CreateOrderResultDTO response = orderService.createOrder(userDetails.getMember().getId(), request);
        return ApiResponse.onSuccess(SuccessStatus.ORDER_CREATED, response);
    }

    @PatchMapping("/{orderId}/complete")
    public ApiResponse<OrderRespDTO.CompleteOrderResultDTO> completeOrder(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long orderId,
            @RequestBody @Valid OrderReqDTO.CompleteOrderDTO request
    ) {
        OrderRespDTO.CompleteOrderResultDTO response =
                orderService.completeOrder(userDetails.getMember().getId(), orderId, request);
        return ApiResponse.onSuccess(SuccessStatus.ORDER_COMPLETED, response);
    }

    @GetMapping("/{orderId}/status")
    public ApiResponse<OrderRespDTO.OrderStatusDTO> getOrderStatus(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long orderId
    ) {
        OrderRespDTO.OrderStatusDTO response =
                orderService.getOrderStatus(userDetails.getMember().getId(), orderId);
        return ApiResponse.onSuccess(SuccessStatus.ORDER_STATUS_FOUND, response);
    }
}