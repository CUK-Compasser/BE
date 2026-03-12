package Comprehensive_Design_Project.CUK_Compasser.domain.reservation.controller;

import Comprehensive_Design_Project.CUK_Compasser.domain.reservation.dto.ReservationReqDTO;
import Comprehensive_Design_Project.CUK_Compasser.domain.reservation.dto.ReservationRespDTO;
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
@RequestMapping("/owners/my-store")
@Tag(name = "Owner Reservation", description = "점장 예약/주문 관리 API")
public class ReservationController {

    private final ReservationService reservationService;

    @Operation(
            summary = "예약 페이지 목록 조회",
            description = "로그인한 점장의 가게 기준으로 확인 대기중(REQUESTED) 예약 목록만 조회합니다."
    )
    @GetMapping("/reservations")
    public ApiResponse<ReservationRespDTO.ReservationListDTO> getPendingReservations(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return ApiResponse.onSuccess(
                SuccessStatus.OK,
                reservationService.getPendingReservations(userDetails.getMember().getId())
        );
    }

    @Operation(
            summary = "주문 페이지 목록 조회",
            description = "로그인한 점장의 가게 기준으로 처리 완료 목록(APPROVED, REJECTED, CANCELED)을 조회합니다."
    )
    @GetMapping("/reservations/processed")
    public ApiResponse<ReservationRespDTO.ReservationListDTO> getProcessedReservations(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return ApiResponse.onSuccess(
                SuccessStatus.OK,
                reservationService.getProcessedReservations(userDetails.getMember().getId())
        );
    }

    @Operation(
            summary = "예약 수락",
            description = "로그인한 점장의 가게 기준으로 확인 대기중 예약을 수락합니다."
    )
    @PatchMapping("/reservations/{reservationId}/approve")
    public ApiResponse<ReservationRespDTO.ReservationDTO> approveReservation(
            @PathVariable Long reservationId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return ApiResponse.onSuccess(
                SuccessStatus.OK,
                reservationService.approveReservation(reservationId, userDetails.getMember().getId())
        );
    }

    @Operation(
            summary = "예약 거절",
            description = "로그인한 점장의 가게 기준으로 확인 대기중 예약을 거절합니다."
    )
    @DeleteMapping("/reservations/{reservationId}/reject")
    public ApiResponse<ReservationRespDTO.ReservationDTO> rejectReservation(
            @PathVariable Long reservationId,
            @RequestBody ReservationReqDTO.RejectDTO request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return ApiResponse.onSuccess(
                SuccessStatus.OK,
                reservationService.rejectReservation(reservationId, userDetails.getMember().getId(), request)
        );
    }
}