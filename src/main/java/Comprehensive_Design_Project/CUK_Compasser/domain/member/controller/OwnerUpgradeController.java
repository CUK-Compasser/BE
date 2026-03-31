package Comprehensive_Design_Project.CUK_Compasser.domain.member.controller;

import Comprehensive_Design_Project.CUK_Compasser.domain.member.dto.OwnerUpgradeRespDTO;
import Comprehensive_Design_Project.CUK_Compasser.domain.member.service.OwnerUpgradeService;
import Comprehensive_Design_Project.CUK_Compasser.domain.owner.dto.BusinessLicenseVerifyReqDTO;
import Comprehensive_Design_Project.CUK_Compasser.global.common.apiPayload.ApiResponse;
import Comprehensive_Design_Project.CUK_Compasser.global.common.apiPayload.code.status.SuccessStatus;
import Comprehensive_Design_Project.CUK_Compasser.global.security.userDetails.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/owners")
public class OwnerUpgradeController {

    private final OwnerUpgradeService ownerUpgradeService;

    /**
     * 이미 검증된 사업자정보를 기반으로 점장 승격
     */
    @PatchMapping("/upgrade")
    public ApiResponse<OwnerUpgradeRespDTO> upgradeToStoreManager(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long memberId = userDetails.getMember().getId();

        return ApiResponse.onSuccess(
                SuccessStatus.OK,
                ownerUpgradeService.upgradeToStoreManager(memberId)
        );
    }

    /**
     * 사업자등록정보 진위확인 + 점장 승격 원스텝
     */
    @PostMapping("/auth/business-license/verify")
    public ApiResponse<OwnerUpgradeRespDTO> verifyBizAndUpgrade(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody @Valid BusinessLicenseVerifyReqDTO req
    ) {
        Long memberId = userDetails.getMember().getId();

        return ApiResponse.onSuccess(
                ownerUpgradeService.verifyBusinessLicenseAndUpgrade(memberId, req)
        );
    }
}