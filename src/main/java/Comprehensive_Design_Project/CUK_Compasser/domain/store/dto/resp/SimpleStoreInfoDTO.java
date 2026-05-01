package Comprehensive_Design_Project.CUK_Compasser.domain.store.dto.resp;

import Comprehensive_Design_Project.CUK_Compasser.domain.store.entity.Tag;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SimpleStoreInfoDTO {
    private Long storeId;
    private Tag tag;
    private String storeName;
    private String storeEmail;
    private String roadAddress;
    private String jibunAddress;
    private JsonNode businessHours;
}
