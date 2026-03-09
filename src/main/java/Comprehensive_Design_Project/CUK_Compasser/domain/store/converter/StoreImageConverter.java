package Comprehensive_Design_Project.CUK_Compasser.domain.store.converter;

import Comprehensive_Design_Project.CUK_Compasser.domain.store.dto.resp.StoreImageRespDTO;
import Comprehensive_Design_Project.CUK_Compasser.domain.store.entity.StoreImage;
import org.springframework.stereotype.Component;

@Component
public class StoreImageConverter {

    public StoreImageRespDTO toResp(StoreImage image) {
        return StoreImageRespDTO.builder()
                .imageId(image.getId())
                .storeId(image.getStore().getId())
                .imageUrl(image.getImageUrl())
                .createdAt(image.getCreatedAt())
                .isDefault(false)
                .build();
    }

    public StoreImageRespDTO toDefaultResp(Long storeId, String defaultImageUrl) {
        return StoreImageRespDTO.builder()
                .imageId(null)
                .storeId(storeId)
                .imageUrl(defaultImageUrl)
                .createdAt(null)
                .isDefault(true)
                .build();
    }
}