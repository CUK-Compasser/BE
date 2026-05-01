package Comprehensive_Design_Project.CUK_Compasser.domain.reservation.controller;

import Comprehensive_Design_Project.CUK_Compasser.domain.reservation.dto.KakaoPayRespDTO;
import Comprehensive_Design_Project.CUK_Compasser.domain.reservation.service.ReservationService;
import Comprehensive_Design_Project.CUK_Compasser.global.common.apiPayload.ApiResponse;
import Comprehensive_Design_Project.CUK_Compasser.global.common.apiPayload.code.status.SuccessStatus;
import Comprehensive_Design_Project.CUK_Compasser.global.security.userDetails.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reservations")
@Tag(name = "Reservation Payment", description = "사용자 예약 결제 API")
public class ReservationPaymentController {

    private final ReservationService reservationService;

    @Operation(
            summary = "카카오페이 결제 준비",
            description = "사용자 본인 예약 기준으로 카카오페이 결제창 진입을 위한 tid와 redirectUrl을 생성합니다."
    )
    @PostMapping("/{reservationId}/payment/ready")
    public ApiResponse<KakaoPayRespDTO.ReadyResultDTO> readyKakaoPay(
            @PathVariable Long reservationId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return ApiResponse.onSuccess(
                SuccessStatus.RESERVATION_PAYMENT_READY,
                reservationService.readyKakaoPay(reservationId, userDetails.getMember().getId())
        );
    }

    @Operation(
            summary = "카카오페이 결제창 진입 전 취소",
            description = "결제창에서 사용자가 취소한 경우 결제 상태만 CANCELED로 변경합니다."
    )
    @PostMapping("/{reservationId}/payment/cancel")
    public ApiResponse<Void> cancelKakaoPay(
            @PathVariable Long reservationId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        reservationService.cancelKakaoPay(reservationId, userDetails.getMember().getId());
        return ApiResponse.onSuccess(SuccessStatus.OK, null);
    }
}