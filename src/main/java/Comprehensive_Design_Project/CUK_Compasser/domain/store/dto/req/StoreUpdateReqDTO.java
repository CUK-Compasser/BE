package Comprehensive_Design_Project.CUK_Compasser.domain.store.dto.req;

import Comprehensive_Design_Project.CUK_Compasser.domain.member.entity.BankType;
import Comprehensive_Design_Project.CUK_Compasser.domain.store.entity.Tag;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class StoreUpdateReqDTO {

    private String storeName;
    private String storeEmail;

    private BankType bankType;
    private String depositor;
    private String bankAccount;

    private JsonNode businessHours;
    private Tag tag;
}