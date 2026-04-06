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
                .price(box.getPrice())
                .buyLimit(box.getBuyLimit())
                .saleStatus(box.getSaleStatus().name())
                .pickupTimeInfo(box.getPickupTimeInfo())
                .build();
    }
}