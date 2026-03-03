package Comprehensive_Design_Project.CUK_Compasser.domain.store.converter;

import Comprehensive_Design_Project.CUK_Compasser.domain.store.dto.StoreRespDTO;
import Comprehensive_Design_Project.CUK_Compasser.domain.store.entity.Store;
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
        return new StoreRespDTO(
                store.getId(),
                store.getStoreManager().getMemberId(), // ✅ PK=FK
                store.getStoreName(),
                store.getStoreDetails(),
                store.getBeforePrice(),
                store.getAfterPrice(),
                store.getLatitude(),
                store.getLongitude(),
                toJsonNode(store.getBusinessHours())
        );
    }

    public String toRawJson(JsonNode node) {
        try {
            return objectMapper.writeValueAsString(node);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("businessHours JSON 직렬화 실패", e);
        }
    }

    private JsonNode toJsonNode(String rawJson) {
        if (rawJson == null || rawJson.isBlank()) return objectMapper.createObjectNode();
        try {
            return objectMapper.readTree(rawJson);
        } catch (JsonProcessingException e) {
            return objectMapper.createObjectNode();
        }
    }
}