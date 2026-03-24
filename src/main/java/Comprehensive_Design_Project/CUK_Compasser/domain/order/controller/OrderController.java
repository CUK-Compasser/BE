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
     * - 사용자가 랜덤박스와 수량을 선택해 주문을 생성한다.
     * - 내부적으로 Reservation을 생성하여 사용자 주문 화면에서 사용한다.
     */
    @PostMapping
    public ApiResponse<OrderRespDTO.CreateOrderResultDTO> createOrder(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody @Valid OrderReqDTO.CreateOrderDTO request
    ) {
        OrderRespDTO.CreateOrderResultDTO response =
                orderService.createOrder(userDetails.getMember().getId(), request);

        return ApiResponse.onSuccess(SuccessStatus.ORDER_CREATED, response);
    }

    /**
     * 사용자 송금 완료 처리 API
     * - 사용자가 '송금 완료' 버튼을 눌렀을 때 호출된다.
     * - 주문의 결제 상태를 PENDING -> PAID 로 변경한다.
     */
    @PatchMapping("/{orderId}/complete")
    public ApiResponse<OrderRespDTO.CompleteOrderResultDTO> completeOrder(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long orderId
    ) {
        OrderRespDTO.CompleteOrderResultDTO response =
                orderService.completeOrder(userDetails.getMember().getId(), orderId);

        return ApiResponse.onSuccess(SuccessStatus.ORDER_COMPLETED, response);
    }

    /**
     * 사용자 주문 취소 API
     * - 사용자가 자신의 주문을 취소할 때 호출된다.
     * - 내부적으로 Reservation 상태를 REQUESTED -> CANCELED 로 변경한다.
     * - 아직 점주가 처리하지 않은 주문만 취소 가능하도록 제한할 수 있다.
     */
    @PatchMapping("/{orderId}/cancel")
    public ApiResponse<OrderRespDTO.CancelOrderResultDTO> cancelOrder(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long orderId
    ) {
        OrderRespDTO.CancelOrderResultDTO response =
                orderService.cancelOrder(userDetails.getMember().getId(), orderId);

        return ApiResponse.onSuccess(SuccessStatus.ORDER_CANCELED, response);
    }

    /**
     * 사용자 주문 상태 조회 API
     * - 사용자가 자신의 주문 상세 상태를 조회한다.
     * - 외부 API에서는 orderId 개념으로 사용하지만 내부적으로는 Reservation 데이터를 조회한다.
     */
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