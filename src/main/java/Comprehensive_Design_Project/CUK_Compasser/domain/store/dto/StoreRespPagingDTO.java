package Comprehensive_Design_Project.CUK_Compasser.domain.store.dto;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.*;

import java.math.BigDecimal;

public class StoreRespPagingDTO {

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class GetStoreOrderByCreatedDTO {
        private Long storeId;
        private Long storeManagerId;
        private String storeName;
        private String storeDetails;
        private BigDecimal latitude;
        private BigDecimal longitude;
        private JsonNode businessHours;
    }
}
