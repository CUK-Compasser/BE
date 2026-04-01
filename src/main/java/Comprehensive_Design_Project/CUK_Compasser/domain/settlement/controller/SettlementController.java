package Comprehensive_Design_Project.CUK_Compasser.domain.settlement.controller;

import Comprehensive_Design_Project.CUK_Compasser.domain.settlement.dto.SettlementReqDTO;
import Comprehensive_Design_Project.CUK_Compasser.domain.settlement.dto.SettlementRespDTO;
import Comprehensive_Design_Project.CUK_Compasser.domain.settlement.service.SettlementService;
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
@RequestMapping("/owners/my-store/settlements")
@Tag(name = "Owner Settlement", description = "점장 정산 조회 API")
public class SettlementController {

    private final SettlementService settlementService;

    @Operation(summary = "정산 대상 미리보기", description = "내 가게의 정산 가능한 주문 목록과 총 금액을 조회합니다.")
    @GetMapping("/preview")
    public ApiResponse<SettlementRespDTO.SettlementPreviewDTO> previewSettlement(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return ApiResponse.onSuccess(
                SuccessStatus.OK,
                settlementService.previewSettlement(userDetails.getMember().getId())
        );
    }
}