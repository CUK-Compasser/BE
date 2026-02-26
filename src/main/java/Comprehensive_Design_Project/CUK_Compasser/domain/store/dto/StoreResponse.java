package Comprehensive_Design_Project.CUK_Compasser.domain.store.dto;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class StoreResponse {

    private Long storeId;
    private Long storeManagerId;

    private String storeName;
    private String storeDetails;
    private Integer beforePrice;
    private Integer afterPrice;

    private BigDecimal latitude;
    private BigDecimal longitude;

    private JsonNode businessHours;
}