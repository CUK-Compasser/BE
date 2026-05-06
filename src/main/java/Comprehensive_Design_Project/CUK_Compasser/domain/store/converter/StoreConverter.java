package Comprehensive_Design_Project.CUK_Compasser.domain.store.converter;

import Comprehensive_Design_Project.CUK_Compasser.domain.randomBox.converter.RandomBoxConverter;
import Comprehensive_Design_Project.CUK_Compasser.domain.randomBox.dto.RandomBoxRespDTO;
import Comprehensive_Design_Project.CUK_Compasser.domain.randomBox.entity.RandomBox;
import Comprehensive_Design_Project.CUK_Compasser.domain.store.dto.resp.StoreDetailsDTO;
import Comprehensive_Design_Project.CUK_Compasser.domain.store.dto.resp.StoreRespDTO;
import Comprehensive_Design_Project.CUK_Compasser.domain.store.dto.resp.StoreRespPagingDTO;
import Comprehensive_Design_Project.CUK_Compasser.domain.store.entity.Store;
import Comprehensive_Design_Project.CUK_Compasser.global.common.apiPayload.code.status.ErrorStatus;
import Comprehensive_Design_Project.CUK_Compasser.global.common.apiPayload.exception.GeneralException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class StoreConverter {

    private final ObjectMapper objectMapper;

    public StoreRespDTO toResp(Store store) {
        return StoreRespDTO.builder()
                .storeId(store.getId())
                .storeManagerId(store.getStoreManager().getId())
                .storeName(store.getStoreName())
                .inputAddress(store.getInputAddress())
                .roadAddress(store.getRoadAddress())
                .jibunAddress(store.getJibunAddress())
                .storeEmail(store.getStoreEmail())
                .latitude(store.getLatitude())
                .longitude(store.getLongitude())
                .businessHours(toJsonNode(store.getBusinessHours()))
                .tag(store.getTag())
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

    // 일단 기본 조회 기준으로 converter를 만들긴 했는데, 재사용 가능하게 수정 예정
    public List<StoreRespPagingDTO.GetStoreReqDTO> toGetStoreDTOList(List<Store> storeList) {
        List<StoreRespPagingDTO.GetStoreReqDTO> dtoList = new ArrayList<>();

        for (Store store : storeList) {
            String imageUrl = (store.getImages() != null && !store.getImages().isEmpty())
                    ? store.getImages().get(0).getImageUrl()
                    : null;

            dtoList.add(StoreRespPagingDTO.GetStoreReqDTO.builder()
                    .storeId(store.getId()) // 맵 화면에서 사용할 id -> 심플 조회 (바텀시트바) API 에 활용
                    .storeManagerId(store.getStoreManager().getId())
                    .storeName(store.getStoreName())
                    .storeImage(imageUrl)
                    .tag(store.getTag())
                    .storeEmail(store.getStoreEmail())
                    .latitude(store.getLatitude())  // 맵 화면에서 사용할 위도 -> 심플 조회 (바텀시트바) API 에 활용
                    .longitude(store.getLongitude())  // 맵 화면에서 사용할 경도 -> 심플 조회 (바텀시트바) API 에 활용
                    .businessHours(toJsonNode(store.getBusinessHours()))
                    .build());
        }
        return dtoList;
    }

    public StoreDetailsDTO toStoreDetailsDTO(Store store) {

        RandomBoxConverter randomBoxConverter = new RandomBoxConverter();

        List<RandomBoxRespDTO> randomBoxes = store.getRandomBoxes().stream()
                .map(randomBoxConverter::toResp)
                .toList();

        return StoreDetailsDTO.builder()
                .storeId(store.getId())
                .storeManagerId(store.getStoreManager().getId()) // 현재 fetch join으로 이미 랜덤박스를 들고 와서 얘는 쿼리가 발생
                .storeName(store.getStoreName())
                .storeEmail(store.getStoreEmail())
                .images(store.getImages())
                .inputAddress(store.getInputAddress())
                .roadAddress(store.getRoadAddress())
                .jibunAddres(store.getJibunAddress())
                .latitude(store.getLatitude())
                .longitude(store.getLongitude())
                .businessHours(toJsonNode(store.getBusinessHours()))
                .tag(store.getTag())
                .randomBoxes(randomBoxes)
                .build();
    }
}