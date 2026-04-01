package Comprehensive_Design_Project.CUK_Compasser.domain.settlement.controller;

import Comprehensive_Design_Project.CUK_Compasser.domain.settlement.dto.SettlementReqDTO;
import Comprehensive_Design_Project.CUK_Compasser.domain.settlement.dto.SettlementRespDTO;
import Comprehensive_Design_Project.CUK_Compasser.domain.settlement.service.SettlementService;
import Comprehensive_Design_Project.CUK_Compasser.global.common.apiPayload.ApiResponse;
import Comprehensive_Design_Project.CUK_Compasser.global.common.apiPayload.code.status.SuccessStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/settlements")
@Tag(name = "Admin Settlement", description = "운영자 정산 관리 API")
public class AdminSettlementController {

    private final SettlementService settlementService;

    @Operation(summary = "정산 완료 처리", description = "운영자가 송금 완료 후 해당 가게 주문들을 정산 완료 처리합니다.")
    @PostMapping("/{storeId}/complete")
    public ApiResponse<SettlementRespDTO.SettlementCompleteDTO> completeSettlement(
            @PathVariable Long storeId,
            @RequestBody SettlementReqDTO.CompleteSettlementDTO request
    ) {
        return ApiResponse.onSuccess(
                SuccessStatus.OK,
                settlementService.completeSettlementByStore(storeId, request)
        );
    }
}