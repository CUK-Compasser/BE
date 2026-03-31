package Comprehensive_Design_Project.CUK_Compasser.domain.settlement.service;

import Comprehensive_Design_Project.CUK_Compasser.domain.settlement.dto.SettlementReqDTO;
import Comprehensive_Design_Project.CUK_Compasser.domain.settlement.dto.SettlementRespDTO;

public interface SettlementService {
    SettlementRespDTO.SettlementPreviewDTO previewSettlement(Long ownerId);

    SettlementRespDTO.SettlementCompleteDTO completeSettlementByStore(Long ownerId, SettlementReqDTO.CompleteSettlementDTO request);
}