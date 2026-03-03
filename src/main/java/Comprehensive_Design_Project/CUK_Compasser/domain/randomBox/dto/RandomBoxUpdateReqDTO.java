package Comprehensive_Design_Project.CUK_Compasser.domain.randomBox.dto;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RandomBoxUpdateRequest {
    private String boxName;
    private Integer stock;
    private Integer beforePrice;
    private Integer afterPrice;
    private String content;
    private String saleStatus;
}