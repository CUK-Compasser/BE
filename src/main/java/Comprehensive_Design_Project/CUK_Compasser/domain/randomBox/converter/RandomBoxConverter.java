package Comprehensive_Design_Project.CUK_Compasser.domain.randomBox.converter;

import Comprehensive_Design_Project.CUK_Compasser.domain.randomBox.dto.RandomBoxRespDTO;
import Comprehensive_Design_Project.CUK_Compasser.domain.randomBox.entity.RandomBox;
import org.springframework.stereotype.Component;

@Component
public class RandomBoxConverter {

    public RandomBoxRespDTO toResp(RandomBox box) {
        return RandomBoxRespDTO.builder()
                .boxId(box.getId())
                .storeId(box.getStore().getId())
                .boxName(box.getBoxName())
                .content(box.getContent())
                .stock(box.getStock())
                .beforePrice(box.getBeforePrice())
                .afterPrice(box.getAfterPrice())
                .saleStatus(box.getSaleStatus().name()) // ✅ enum -> String
                .build();
    }
}