package Comprehensive_Design_Project.CUK_Compasser.domain.store.dto.resp;

import Comprehensive_Design_Project.CUK_Compasser.domain.store.entity.Tag;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreRespDTO {

    private Long storeId;
    private Long storeManagerId;
    private String storeName;
    private String storeEmail;

    private String inputAddress;
    private String roadAddress;
    private String jibunAddress;

    private BigDecimal latitude;
    private BigDecimal longitude;

    private JsonNode businessHours;

    private Tag tag;
}