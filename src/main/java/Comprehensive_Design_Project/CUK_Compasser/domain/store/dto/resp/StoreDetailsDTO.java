package Comprehensive_Design_Project.CUK_Compasser.domain.store.dto.resp;

import Comprehensive_Design_Project.CUK_Compasser.domain.randomBox.dto.RandomBoxRespDTO;
import Comprehensive_Design_Project.CUK_Compasser.domain.store.entity.StoreImage;
import Comprehensive_Design_Project.CUK_Compasser.domain.store.entity.Tag;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;

@Builder
public record StoreDetailsDTO(
        Long storeId,
        Long storeManagerId,
        String storeName,
        String storeEmail,
        List<StoreImage> images,

        String inputAddress,
        String roadAddress,
        String jibunAddres,

        BigDecimal latitude,
        BigDecimal longitude,

        JsonNode businessHours,
        Tag tag,

        List<RandomBoxRespDTO> randomBoxes
) {

}
