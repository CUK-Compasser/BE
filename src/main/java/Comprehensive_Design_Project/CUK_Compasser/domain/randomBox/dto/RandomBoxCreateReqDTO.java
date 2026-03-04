package Comprehensive_Design_Project.CUK_Compasser.domain.randomBox.dto;

import Comprehensive_Design_Project.CUK_Compasser.domain.randomBox.entity.SaleStatus;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RandomBoxCreateReqDTO {
    private String boxName;
    private String content;
    private Integer stock;
    private Integer beforePrice;
    private Integer afterPrice;
    private SaleStatus saleStatus; // ✅ enum
}