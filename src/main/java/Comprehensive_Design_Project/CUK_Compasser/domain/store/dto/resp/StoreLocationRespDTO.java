package Comprehensive_Design_Project.CUK_Compasser.domain.store.dto.resp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
@AllArgsConstructor
public class StoreLocationRespDTO {
    private Long storeId;
    private String inputAddress;
    private String roadAddress;
    private String jibunAddress;
    private BigDecimal latitude;
    private BigDecimal longitude;
}