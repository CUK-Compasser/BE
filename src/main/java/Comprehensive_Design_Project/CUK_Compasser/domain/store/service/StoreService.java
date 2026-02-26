package Comprehensive_Design_Project.CUK_Compasser.domain.store.service;

import Comprehensive_Design_Project.CUK_Compasser.domain.store.dto.StoreResponse;
import Comprehensive_Design_Project.CUK_Compasser.domain.store.dto.StoreUpdateRequest;
import Comprehensive_Design_Project.CUK_Compasser.domain.store.entity.Store;
import Comprehensive_Design_Project.CUK_Compasser.domain.store.repository.StoreRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;
    private final ObjectMapper objectMapper;

    /**
     * 점장(스토어매니저)이 자기 가게 정보를 PATCH로 수정
     * - null 필드는 수정하지 않음
     * - businessHours는 raw JSON(JsonNode)로 받고 String으로 저장
     */
    @Transactional
    public StoreResponse updateStore(Long storeId, Long storeManagerId, StoreUpdateRequest req) {

        Store store = storeRepository.findByIdAndStoreManager_Id(storeId, storeManagerId)
                .orElseThrow(() -> new IllegalArgumentException("가게가 없거나 수정 권한이 없습니다. storeId=" + storeId));

        if (req.getStoreName() != null) {
            store.setStoreName(req.getStoreName());
        }
        if (req.getStoreDetails() != null) {
            store.setStoreDetails(req.getStoreDetails());
        }
        if (req.getBeforePrice() != null) {
            store.setBeforePrice(req.getBeforePrice());
        }
        if (req.getAfterPrice() != null) {
            store.setAfterPrice(req.getAfterPrice());
        }

        if (req.getBusinessHours() != null) {
            validateBusinessHours(req.getBusinessHours());
            store.setBusinessHours(toRawJson(req.getBusinessHours()));
        }

        return toResponse(store);
    }

    @Transactional(readOnly = true)
    public StoreResponse getStore(Long storeId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("가게가 없습니다. storeId=" + storeId));
        return toResponse(store);
    }

    private void validateBusinessHours(JsonNode node) {
        // raw를 유지하되, 최소한의 구조는 강제하는 게 안전
        if (node.get("weekly") == null) {
            throw new IllegalArgumentException("businessHours.weekly가 필요합니다.");
        }
    }

    private String toRawJson(JsonNode node) {
        try {
            return objectMapper.writeValueAsString(node);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("businessHours JSON 직렬화 실패", e);
        }
    }

    private JsonNode toJsonNode(String rawJson) {
        if (rawJson == null || rawJson.isBlank()) {
            return objectMapper.createObjectNode();
        }
        try {
            return objectMapper.readTree(rawJson);
        } catch (JsonProcessingException e) {
            return objectMapper.createObjectNode();
        }
    }

    private StoreResponse toResponse(Store store) {
        return new StoreResponse(
                store.getId(),
                store.getStoreManager().getId(),
                store.getStoreName(),
                store.getStoreDetails(),
                store.getBeforePrice(),
                store.getAfterPrice(),
                store.getLatitude(),
                store.getLongitude(),
                toJsonNode(store.getBusinessHours())
        );
    }
}