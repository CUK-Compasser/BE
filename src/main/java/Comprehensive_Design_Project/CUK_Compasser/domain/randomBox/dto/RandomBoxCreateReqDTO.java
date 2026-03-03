package Comprehensive_Design_Project.CUK_Compasser.domain.randomBox.dto;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RandomBoxCreateRequest {
    private String boxName;      // 피그마: 랜덤박스 이름
    private Integer stock;       // 피그마: 총 수량
    private Integer beforePrice;
    private Integer afterPrice;
    private String content;      // 피그마: 설명
    private String saleStatus;   // READY/ON_SALE/SOLD_OUT 등 정책값
}