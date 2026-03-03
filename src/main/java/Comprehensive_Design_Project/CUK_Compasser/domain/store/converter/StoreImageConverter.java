package Comprehensive_Design_Project.CUK_Compasser.domain.store.converter;

import Comprehensive_Design_Project.CUK_Compasser.domain.store.dto.StoreImageRespDTO;
import Comprehensive_Design_Project.CUK_Compasser.domain.store.entity.StoreImage;
import org.springframework.stereotype.Component;

@Component
public class StoreImageConverter {

    public StoreImageRespDTO toResp(StoreImage image) {
        return StoreImageRespDTO.builder()
                .imageId(image.getId())
                .imageUrl(image.getImageUrl())
                .createdAt(image.getCreatedAt())
                .build();
    }
}