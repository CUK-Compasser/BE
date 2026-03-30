package Comprehensive_Design_Project.CUK_Compasser.domain.store.dto.resp;

import Comprehensive_Design_Project.CUK_Compasser.domain.store.entity.Tag;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.*;

import java.math.BigDecimal;

public class StoreRespPagingDTO {

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class GetStoreReqDTO {
        private Long storeId;
        private Long storeManagerId;
        private String storeName;
        private String storeImage;
        private Tag tag;
        private String storeDetails;
        private BigDecimal latitude;
        private BigDecimal longitude;
        private JsonNode businessHours;
    }
}
