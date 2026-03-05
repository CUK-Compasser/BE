package Comprehensive_Design_Project.CUK_Compasser.domain.store.dto;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class StoreUpdateReqDTO {
    private String storeName;

    private String storeEmail;
    private String bankName;//아마 enum으로 빼야할듯
    private String depositor;
    private String bankAccount;

    // businessHours JSON (String으로 받거나 Map으로 받아도 됨)
    private JsonNode businessHours;

}