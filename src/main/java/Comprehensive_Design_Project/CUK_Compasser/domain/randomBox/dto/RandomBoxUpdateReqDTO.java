package Comprehensive_Design_Project.CUK_Compasser.domain.randomBox.dto;

import Comprehensive_Design_Project.CUK_Compasser.domain.randomBox.entity.SaleStatus;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RandomBoxUpdateReqDTO {
    private String boxName;
    private String content;
    private Integer stock;
    private Integer price;
    private Integer buyLimit;
    private SaleStatus saleStatus;

    // pickupTime JSON
    private JsonNode pickupTimeInfo;
}