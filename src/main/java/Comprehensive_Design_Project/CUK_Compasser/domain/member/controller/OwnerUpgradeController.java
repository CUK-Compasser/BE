package Comprehensive_Design_Project.CUK_Compasser.domain.member.controller;

import Comprehensive_Design_Project.CUK_Compasser.domain.member.dto.OwnerUpgradeRespDTO;
import Comprehensive_Design_Project.CUK_Compasser.domain.member.service.OwnerUpgradeService;
import Comprehensive_Design_Project.CUK_Compasser.domain.owner.dto.BusinessLicenseVerifyReqDTO;
import Comprehensive_Design_Project.CUK_Compasser.global.security.userDetails.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/owners")
public class OwnerUpgradeController {

    private final OwnerUpgradeService ownerUpgradeService;

    @PatchMapping("/upgrade")
    public OwnerUpgradeRespDTO upgradeToStoreManager(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long memberId = userDetails.getMember().getId();
        return ownerUpgradeService.upgradeToStoreManager(memberId);
    }

    // ✅ 사업자번호 입력 화면용: 검증 + 승격 원스텝
    @PostMapping("/auth/business-license/verify")
    public OwnerUpgradeRespDTO verifyBizAndUpgrade(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody BusinessLicenseVerifyReqDTO req
    ) {
        Long memberId = userDetails.getMember().getId();
        return ownerUpgradeService.verifyBusinessLicenseAndUpgrade(memberId, req.getBusinessLicenseNumber());
    }
}