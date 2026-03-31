package Comprehensive_Design_Project.CUK_Compasser.domain.member.service;

import Comprehensive_Design_Project.CUK_Compasser.domain.member.dto.OwnerBusinessVerifyReqDTO;
import Comprehensive_Design_Project.CUK_Compasser.domain.member.dto.OwnerUpgradeRespDTO;

public interface OwnerUpgradeService {

    OwnerUpgradeRespDTO upgradeToStoreManager(Long memberId);

    OwnerUpgradeRespDTO verifyBusinessLicenseAndUpgrade(Long memberId, OwnerBusinessVerifyReqDTO request);
}