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

    /**
     * 사용자 주문 생성 API
     */
    @PostMapping
    public ApiResponse<OrderRespDTO.CreateOrderResultDTO> createOrder(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody @Valid OrderReqDTO.CreateOrderDTO request
    ) {
        return ApiResponse.onSuccess(
                SuccessStatus.ORDER_CREATED,
                orderService.createOrder(userDetails.getMember().getId(), request)
        );
    }

    /**
     * 사용자 주문 취소 API
     */
    @PatchMapping("/{orderId}/cancel")
    public ApiResponse<OrderRespDTO.CancelOrderResultDTO> cancelOrder(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long orderId
    ) {
        return ApiResponse.onSuccess(
                SuccessStatus.ORDER_CANCELED,
                orderService.cancelOrder(userDetails.getMember().getId(), orderId)
        );
    }

    /**
     * 사용자 주문 상태 조회 API
     */
    @GetMapping("/{orderId}/status")
    public ApiResponse<OrderRespDTO.OrderStatusDTO> getOrderStatus(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long orderId
    ) {
        return ApiResponse.onSuccess(
                SuccessStatus.ORDER_STATUS_FOUND,
                orderService.getOrderStatus(userDetails.getMember().getId(), orderId)
        );
    }
}