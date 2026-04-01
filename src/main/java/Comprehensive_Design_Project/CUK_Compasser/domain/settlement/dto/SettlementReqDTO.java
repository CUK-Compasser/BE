package Comprehensive_Design_Project.CUK_Compasser.domain.settlement.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class SettlementReqDTO {

    @Getter
    @NoArgsConstructor
    public static class CompleteSettlementDTO {
        private List<Long> reservationIds;
    }
}