package Comprehensive_Design_Project.CUK_Compasser.domain.randomBox.dto;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RandomBoxRespDTO {
    private Long boxId;
    private Long storeId;
    private String boxName;
    private Integer stock;
    private Integer beforePrice;
    private Integer afterPrice;
    private String content;
    private String saleStatus;
}