package Comprehensive_Design_Project.CUK_Compasser.domain.store.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class StoreUpdateReqDTO {
    private String storeName;
    private String storeDetails;

    // 가격(너희 stores 컬럼에 있음)
    private Integer beforePrice;
    private Integer afterPrice;

    // businessHours JSON (String으로 받거나 Map으로 받아도 됨)
    private String businessHours;

    // 아래 필드들은 DB에 컬럼이 없으면 추가 필요(앞에서 말한 부분)
    private String storeEmail;
    private String storeAddress;

    private String bankName;
    private String depositor;
    private String bankAccount;

    // 태그도 컬럼/테이블 없으면 추가 필요
    // private List<String> tags;
}