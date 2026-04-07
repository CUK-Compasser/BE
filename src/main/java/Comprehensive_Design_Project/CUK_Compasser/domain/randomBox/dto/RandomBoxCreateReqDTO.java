package Comprehensive_Design_Project.CUK_Compasser.domain.randomBox.dto;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RandomBoxCreateReqDTO {
    private String boxName;
    private String content;
    private Integer stock;
    private Integer price;
    private Integer buyLimit;

    // pickupTime JSON
    private JsonNode pickupTimeInfo;
}