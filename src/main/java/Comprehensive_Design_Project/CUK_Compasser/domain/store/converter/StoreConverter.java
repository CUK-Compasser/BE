package Comprehensive_Design_Project.CUK_Compasser.domain.store.converter;

import Comprehensive_Design_Project.CUK_Compasser.domain.store.dto.StoreRespDTO;
import Comprehensive_Design_Project.CUK_Compasser.domain.store.entity.Store;
import Comprehensive_Design_Project.CUK_Compasser.global.common.apiPayload.code.status.ErrorStatus;
import Comprehensive_Design_Project.CUK_Compasser.global.common.apiPayload.exception.GeneralException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StoreConverter {

    private final ObjectMapper objectMapper;

    public StoreRespDTO toResp(Store store) {
        return StoreRespDTO.builder()
                .storeId(store.getId())
                .storeManagerId(store.getStoreManager().getId())
                .storeName(store.getStoreName())
                .storeDetails(store.getStoreDetails())
                .inputAddress(store.getInputAddress())
                .roadAddress(store.getRoadAddress())
                .jibunAddress(store.getJibunAddress())
                .latitude(store.getLatitude())
                .longitude(store.getLongitude())
                .businessHours(toJsonNode(store.getBusinessHours()))
                .build();
    }
    public String toRawJson(JsonNode node) {
        try {
            return objectMapper.writeValueAsString(node);
        } catch (JsonProcessingException e) {
            throw new GeneralException(ErrorStatus.STORE_BUSINESS_HOURS_SERIALIZE_FAILED);
        }
    }

    private JsonNode toJsonNode(String rawJson) {
        if (rawJson == null || rawJson.isBlank()) {
            return objectMapper.createObjectNode();
        }
        try {
            return objectMapper.readTree(rawJson);
        } catch (JsonProcessingException e) {
            throw new GeneralException(ErrorStatus.STORE_BUSINESS_HOURS_PARSE_FAILED);
        }
    }
}