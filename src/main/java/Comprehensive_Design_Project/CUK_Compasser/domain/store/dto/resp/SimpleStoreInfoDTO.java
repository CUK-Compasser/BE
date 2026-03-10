package Comprehensive_Design_Project.CUK_Compasser.domain.store.dto.resp;

import Comprehensive_Design_Project.CUK_Compasser.domain.store.entity.Tag;
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
    private String roadAddress;
    private String businessHours;
}
