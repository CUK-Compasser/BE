package Comprehensive_Design_Project.CUK_Compasser.domain.member.controller;

import Comprehensive_Design_Project.CUK_Compasser.domain.member.dto.OwnerBusinessVerifyReqDTO;
import Comprehensive_Design_Project.CUK_Compasser.domain.member.dto.OwnerUpgradeRespDTO;
import Comprehensive_Design_Project.CUK_Compasser.domain.member.service.OwnerUpgradeService;
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
@RequestMapping("/owners")
@Tag(name = "Owner Upgrade", description = "점장 승격 및 사업자등록정보 진위확인 API")
public class OwnerUpgradeController {

    private final OwnerUpgradeService ownerUpgradeService;

    /**
     * 이미 검증된 사업자정보를 기반으로 점장 승격
     */
    @Operation(
            summary = "점장 승격 API (개발용)",
            description = """
                    개발 및 내부 테스트용 점장 승격 API입니다.
                    이미 사업자등록정보 검증이 안된 사용자를 STORE_MANAGER로 승격시키고,
                    store_manager 및 기본 store 생성을 보장합니다.
                    
                    실제 운영 환경에서는 사용하지 않고,
                    /owners/auth/business-license/verify API를 통해
                    사업자등록정보 진위확인 후 승격하는 방식을 사용합니다.
                    """
    )
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
    @Operation(
            summary = "사업자등록정보 진위확인 후 점장 승격",
            description = """
                    사업자등록번호, 대표자명, 개업일자를 이용해 국세청 사업자등록정보 진위확인을 수행한 뒤,
                    검증이 성공하면 사용자를 STORE_MANAGER로 승격시키고
                    store_manager 및 기본 store를 생성합니다.
                    """
    )
    @PostMapping("/auth/business-license/verify")
    public ApiResponse<OwnerUpgradeRespDTO> verifyBizAndUpgrade(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody @Valid OwnerBusinessVerifyReqDTO req
    ) {
        Long memberId = userDetails.getMember().getId();

        return ApiResponse.onSuccess(
                SuccessStatus.OK,
                ownerUpgradeService.verifyBusinessLicenseAndUpgrade(memberId, req)
        );
    }
}