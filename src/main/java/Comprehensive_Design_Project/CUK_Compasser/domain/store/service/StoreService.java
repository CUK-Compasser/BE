package Comprehensive_Design_Project.CUK_Compasser.domain.store.service;

import Comprehensive_Design_Project.CUK_Compasser.domain.store.dto.StoreResponse;
import Comprehensive_Design_Project.CUK_Compasser.domain.store.dto.StoreUpdateRequest;
import Comprehensive_Design_Project.CUK_Compasser.domain.store.entity.Store;
import Comprehensive_Design_Project.CUK_Compasser.domain.store.repository.StoreRepository;
import Comprehensive_Design_Project.CUK_Compasser.domain.storeManager.entity.StoreManager;
import Comprehensive_Design_Project.CUK_Compasser.domain.storeManager.repository.StoreManagerRepository;
import Comprehensive_Design_Project.CUK_Compasser.global.common.apiPayload.code.status.ErrorStatus;
import Comprehensive_Design_Project.CUK_Compasser.global.common.apiPayload.exception.GeneralException;
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
    private final StoreManagerRepository storeManagerRepository;
    private final ObjectMapper objectMapper;

    @Transactional
    public StoreResponse updateStore(Long storeId, Long memberId, StoreUpdateRequest req) {

        StoreManager storeManager = storeManagerRepository.findByMemberId(memberId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.STORE_MANAGER_NOT_FOUND));

        Store store = storeRepository.findByIdAndStoreManager_MemberId(storeId, storeManager.getMemberId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.STORE_FORBIDDEN));
        // 권한 없는 경우+없는 경우를 합쳐서 403으로 처리(보안상 더 안전)
        // 만약 "없는 가게"와 "권한 없음"을 분리하고 싶으면 조회를 2번 해야 함.

        if (req.getStoreName() != null) store.setStoreName(req.getStoreName());
        if (req.getStoreDetails() != null) store.setStoreDetails(req.getStoreDetails());

        if (req.getBusinessHours() != null) {
            validateBusinessHours(req.getBusinessHours());
            store.setBusinessHours(toRawJson(req.getBusinessHours()));
        }

        return toResponse(store);
    }

    @Transactional(readOnly = true)
    public StoreResponse getMyStore(Long storeId, Long memberId) {

        StoreManager storeManager = storeManagerRepository.findByMemberId(memberId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.STORE_MANAGER_NOT_FOUND));

        Store store = storeRepository.findByIdAndStoreManager_MemberId(storeId, storeManager.getMemberId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.STORE_FORBIDDEN));

        return toResponse(store);
    }

    private void validateBusinessHours(JsonNode node) {
        // 최소 검증만 (raw 전략)
        if (node.get("weekly") == null) {
            throw new GeneralException(ErrorStatus.BUSINESS_HOURS_INVALID);
        }
    }

    private String toRawJson(JsonNode node) {
        try {
            return objectMapper.writeValueAsString(node);
        } catch (JsonProcessingException e) {
            throw new GeneralException(ErrorStatus.BUSINESS_HOURS_INVALID);
        }
    }

    private JsonNode toJsonNode(String rawJson) {
        if (rawJson == null || rawJson.isBlank()) return objectMapper.createObjectNode();
        try {
            return objectMapper.readTree(rawJson);
        } catch (JsonProcessingException e) {
            // 저장된 값이 깨졌으면 빈 객체로
            return objectMapper.createObjectNode();
        }
    }

    private StoreResponse toResponse(Store store) {
        return new StoreResponse(
                store.getId(),
                store.getStoreManager().getMemberId(),
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