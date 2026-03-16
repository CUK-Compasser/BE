package Comprehensive_Design_Project.CUK_Compasser.domain.member.dto;

import lombok.*;

import java.math.BigDecimal;

public class AddressDTOs {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @Builder
    public static class AddressReqDTO{
        private BigDecimal latitude;
        private BigDecimal longitude;
        private String addressName;
    }
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AddressRespDTO{
        private BigDecimal latitude;
        private BigDecimal longitude;
        private String addressName;
    }
}
