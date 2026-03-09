package Comprehensive_Design_Project.CUK_Compasser.domain.reservation.controller;

import Comprehensive_Design_Project.CUK_Compasser.domain.reservation.dto.ReservationReqDTO;
import Comprehensive_Design_Project.CUK_Compasser.domain.reservation.dto.ReservationRespDTO;
import Comprehensive_Design_Project.CUK_Compasser.domain.reservation.entity.ReservationStatus;
import Comprehensive_Design_Project.CUK_Compasser.domain.reservation.service.ReservationService;
import Comprehensive_Design_Project.CUK_Compasser.global.common.apiPayload.ApiResponse;
import Comprehensive_Design_Project.CUK_Compasser.global.security.userDetails.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @GetMapping("/stores/{storeId}/reservations")
    public ApiResponse<ReservationRespDTO.ReservationListDTO> getReservations(
            @PathVariable Long storeId,
            @RequestParam(required = false) ReservationStatus status,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return ApiResponse.onSuccess(
                reservationService.getReservations(storeId, userDetails.getMemberId(), status)
        );
    }

    @GetMapping("/stores/{storeId}/reservations/{reservationId}")
    public ApiResponse<ReservationRespDTO.ReservationDetailDTO> getReservationDetail(
            @PathVariable Long storeId,
            @PathVariable Long reservationId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return ApiResponse.onSuccess(
                reservationService.getReservationDetail(storeId, reservationId, userDetails.getMemberId())
        );
    }

    @PatchMapping("/reservations/{reservationId}/approve")
    public ApiResponse<ReservationRespDTO.ReservationDetailDTO> approveReservation(
            @PathVariable Long reservationId,
            @RequestBody ReservationReqDTO.ApproveDTO request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return ApiResponse.onSuccess(
                reservationService.approveReservation(reservationId, userDetails.getMemberId(), request)
        );
    }

    @DeleteMapping("/reservations/{reservationId}/reject")
    public ApiResponse<ReservationRespDTO.ReservationDetailDTO> rejectReservation(
            @PathVariable Long reservationId,
            @RequestBody ReservationReqDTO.RejectDTO request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return ApiResponse.onSuccess(
                reservationService.rejectReservation(reservationId, userDetails.getMemberId(), request)
        );
    }
}