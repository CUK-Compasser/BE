package Comprehensive_Design_Project.CUK_Compasser.domain.store.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreLocationUpdateReqDTO {
    private String locationType;      // 예: CURRENT, CUSTOM 등
    private BigDecimal latitude;
    private BigDecimal longitude;
}