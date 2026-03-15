package Comprehensive_Design_Project.CUK_Compasser.domain.store.service;

import Comprehensive_Design_Project.CUK_Compasser.domain.member.dto.MemberReqDTO;
import Comprehensive_Design_Project.CUK_Compasser.domain.store.converter.StoreConverter;
import Comprehensive_Design_Project.CUK_Compasser.domain.store.dto.resp.*;
import Comprehensive_Design_Project.CUK_Compasser.domain.store.entity.Store;
import Comprehensive_Design_Project.CUK_Compasser.domain.store.entity.Tag;
import Comprehensive_Design_Project.CUK_Compasser.domain.store.repository.StoreRepository;
import Comprehensive_Design_Project.CUK_Compasser.domain.storeManager.repository.StoreManagerRepository;
import Comprehensive_Design_Project.CUK_Compasser.global.common.apiPayload.code.status.ErrorStatus;
import Comprehensive_Design_Project.CUK_Compasser.global.common.apiPayload.exception.GeneralException;
import Comprehensive_Design_Project.CUK_Compasser.global.integration.kakao.kakao.dto.KakaoAddressSearchRespDTO;
import Comprehensive_Design_Project.CUK_Compasser.global.integration.kakao.kakao.service.KakaoLocalService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;
    private final StoreManagerRepository storeManagerRepository;
    private final StoreConverter storeConverter;
    private final KakaoLocalService kakaoLocalService;

    private final ObjectMapper objectMapper;

    @Transactional
    public StoreRespDTO updateStore(Long memberId, StoreUpdateReqDTO req) {
        Store store = getMyStoreEntity(memberId);

        if (req.getStoreName() != null) {
            store.setStoreName(req.getStoreName());
        }

        if (req.getStoreEmail() != null) {
            store.setStoreEmail(req.getStoreEmail());
        }

        if (req.getBusinessHours() != null) {
            validateBusinessHours(req.getBusinessHours());
            store.setBusinessHours(storeConverter.toRawJson(req.getBusinessHours()));
        }

        return storeConverter.toResp(store);
    }

    @Transactional(readOnly = true)
    public StoreRespDTO getMyStore(Long memberId) {
        Store store = getMyStoreEntity(memberId);
        return storeConverter.toResp(store);
    }

    @Transactional
    public StoreRespDTO updateLocation(Long memberId, StoreLocationUpdateReqDTO req) {
        Store store = getMyStoreEntity(memberId);

        if (req == null || !StringUtils.hasText(req.getInputAddress())) {
            throw new GeneralException(ErrorStatus.STORE_ADDRESS_NOT_FOUND);
        }

        KakaoAddressSearchRespDTO.Document document =
                kakaoLocalService.searchAddress(req.getInputAddress());

        store.setInputAddress(req.getInputAddress());
        store.setRoadAddress(document.getRoadAddress() != null ? document.getRoadAddress().getAddressName() : null);
        store.setJibunAddress(document.getAddress() != null ? document.getAddress().getAddressName() : null);
        store.setLongitude(new BigDecimal(document.getX()));
        store.setLatitude(new BigDecimal(document.getY()));

        return storeConverter.toResp(store);
    }

    private Store getMyStoreEntity(Long memberId) {
        if (!storeManagerRepository.existsById(memberId)) {
            throw new GeneralException(ErrorStatus.STORE_MANAGER_NOT_FOUND);
        }

        return storeRepository.findByStoreManager_MemberId(memberId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.STORE_NOT_FOUND));
    }

    private void validateBusinessHours(JsonNode node) {
        if (node == null || node.get("weekly") == null) {
            throw new GeneralException(ErrorStatus.BUSINESS_HOURS_INVALID);
        }
    }

    @Transactional (readOnly = true)
    public List<StoreRespPagingDTO.GetStoreReqDTO> getStoreList(BigDecimal userLat, BigDecimal userLon, int page) {
        List<Store> storeList = storeRepository.findStoresWithinRadius( userLon, userLat, 3000, PageRequest.of(page, 10)).getContent();
//        List<Store> allByOrderByCreatedAtDesc = storeRepository.findAllByOrderByCreatedAtDesc(PageRequest.of(page, 10)).getContent();
        return storeConverter.toGetStoreDTOList(storeList);
    }

    @Transactional (readOnly = true)
    public List<StoreRespPagingDTO.GetStoreReqDTO> getStoreListByTag (BigDecimal userLat, BigDecimal userLon, Tag tag, int page){
//        List<Store> allByTag = storeRepository.findAllByTagOrderByCreatedAtDesc(tag, PageRequest.of(page, 10)).getContent();
        List<Store> storeList = storeRepository.findStoresByTagWithinRadius(
                userLon,
                userLat,
                3000,
                tag.toString(),
                PageRequest.of(page, 10)).getContent();
        return storeConverter.toGetStoreDTOList(storeList);
    }

    @Transactional (readOnly = true)
    public List<StoreRespDTO> getStoreListByUniversity (String email, String university){

        return null;
    }

    @Transactional (readOnly = true)
    public List<StoreRespDTO> getStoreListByMemberRadius (String email, MemberReqDTO.MemberCoordinatesDTO coordinates){

        return null;
    }

    @Transactional (readOnly = true)
    public SimpleStoreInfoDTO getSimpleStoreInfo (Long storeId){
        Store store = storeRepository.findById(storeId).orElseThrow(() -> new GeneralException(ErrorStatus.STORE_NOT_FOUND));

        if (store.getBusinessHours() == null || store.getBusinessHours().isBlank()) {
            throw new GeneralException(ErrorStatus.BUSINESS_HOURS_INVALID);
        }
        try {
            return SimpleStoreInfoDTO.builder()
                    .storeId(store.getId())
                    .tag(store.getTag())
                    .storeName(store.getStoreName())
                    .roadAddress(store.getRoadAddress())
                    .jibunAddress(store.getJibunAddress())
                    .businessHours(objectMapper.readTree(store.getBusinessHours())).build();
        } catch (JsonProcessingException e) {
            throw new GeneralException(ErrorStatus.STORE_BUSINESS_HOURS_PARSE_FAILED);
        }
    }

    @Transactional (readOnly = true)
    public StoreRespDTO getStoreInfo (Long storeId){
        return storeConverter.toResp(storeRepository.findById(storeId).orElseThrow(() -> new GeneralException(ErrorStatus.STORE_NOT_FOUND)));
    }

    @Transactional (readOnly = true)
    public List<StoreRespPagingDTO.GetStoreReqDTO> getStoreListByKeyword (BigDecimal userLat, BigDecimal userLon, String keyword, int page){
        List<Store> content = storeRepository.findStoresByKeywordWithinRadius( userLon, userLat, 3000, "%" + keyword + "%", PageRequest.of(page, 10)).getContent();
        return storeConverter.toGetStoreDTOList(content);
    }

}