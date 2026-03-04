package Comprehensive_Design_Project.CUK_Compasser.domain.store.dto;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class StoreUpdateReqDTO {
    private String storeName;
    private String storeDetails;
    private Integer beforePrice;
    private Integer afterPrice;

    /**
     * raw JSON
     * { "timezone":"Asia/Seoul", "weekly": {...} }
     */
    private JsonNode businessHours;
}