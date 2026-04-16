package Comprehensive_Design_Project.CUK_Compasser.domain.order.controller;

import Comprehensive_Design_Project.CUK_Compasser.domain.order.dto.OrderReqDTO;
import Comprehensive_Design_Project.CUK_Compasser.domain.order.dto.OrderRespDTO;
import Comprehensive_Design_Project.CUK_Compasser.domain.order.service.OrderService;
import Comprehensive_Design_Project.CUK_Compasser.global.common.apiPayload.ApiResponse;
import Comprehensive_Design_Project.CUK_Compasser.global.common.apiPayload.code.status.SuccessStatus;
import Comprehensive_Design_Project.CUK_Compasser.global.security.userDetails.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
@Tag(name = "Order", description = "사용자 주문 API")
public class OrderController {

    private final OrderService orderService;

    /**
     * 사용자 주문 생성 API
     */
    @Operation(
            summary = "사용자 주문 생성",
            description = "사용자가 랜덤박스를 선택하고 수량을 입력하여 주문을 생성합니다."
    )
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
    @Operation(
            summary = "사용자 주문 취소",
            description = "사용자가 자신의 주문을 취소합니다. 아직 처리되지 않은 주문만 취소할 수 있습니다."
    )
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
     * 사용자 주문 리스트 조회 API
     */
    @Operation(
            summary = "사용자 주문 리스트 조회",
            description = "로그인한 사용자의 주문 목록을 최신순으로 조회합니다."
    )
    @GetMapping
    public ApiResponse<OrderRespDTO.MemberOrderListDTO> getOrderList(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return ApiResponse.onSuccess(
                SuccessStatus.ORDER_STATUS_FOUND,
                orderService.getOrderList(userDetails.getMember().getId())
        );
    }
}