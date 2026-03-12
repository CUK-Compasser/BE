package Comprehensive_Design_Project.CUK_Compasser.domain.reservation.controller;

import Comprehensive_Design_Project.CUK_Compasser.domain.reservation.dto.ReservationReqDTO;
import Comprehensive_Design_Project.CUK_Compasser.domain.reservation.dto.ReservationRespDTO;
import Comprehensive_Design_Project.CUK_Compasser.domain.reservation.entity.ReservationStatus;
import Comprehensive_Design_Project.CUK_Compasser.domain.reservation.service.ReservationService;
import Comprehensive_Design_Project.CUK_Compasser.global.common.apiPayload.ApiResponse;
import Comprehensive_Design_Project.CUK_Compasser.global.common.apiPayload.code.status.SuccessStatus;
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
                SuccessStatus.OK,
                reservationService.getReservations(storeId, userDetails.getMember().getId(), status)
        );
    }

    @GetMapping("/stores/{storeId}/reservations/{reservationId}")
    public ApiResponse<ReservationRespDTO.ReservationDTO> getReservationDetail(
            @PathVariable Long storeId,
            @PathVariable Long reservationId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return ApiResponse.onSuccess(
                SuccessStatus.OK,
                reservationService.getReservationDetail(storeId, reservationId, userDetails.getMember().getId())
        );
    }

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