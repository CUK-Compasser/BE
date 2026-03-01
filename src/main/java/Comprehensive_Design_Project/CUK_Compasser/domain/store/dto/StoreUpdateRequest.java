package Comprehensive_Design_Project.CUK_Compasser.domain.store.dto;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class StoreUpdateRequest {

    private String storeName;
    private String storeDetails;

    /**
     * raw JSON으로 받는다.
     * 예: { "timezone":"Asia/Seoul", "weekly": {...} }
     */
    private JsonNode businessHours;
}