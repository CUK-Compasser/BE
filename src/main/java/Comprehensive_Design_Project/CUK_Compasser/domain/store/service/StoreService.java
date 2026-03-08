package Comprehensive_Design_Project.CUK_Compasser.domain.store.service;

import Comprehensive_Design_Project.CUK_Compasser.domain.member.dto.MemberReqDTO;
import Comprehensive_Design_Project.CUK_Compasser.domain.store.converter.StoreConverter;
import Comprehensive_Design_Project.CUK_Compasser.domain.store.dto.StoreLocationUpdateReqDTO;
import Comprehensive_Design_Project.CUK_Compasser.domain.store.dto.StoreRespDTO;
import Comprehensive_Design_Project.CUK_Compasser.domain.store.dto.StoreRespPagingDTO;
import Comprehensive_Design_Project.CUK_Compasser.domain.store.dto.StoreUpdateReqDTO;
import Comprehensive_Design_Project.CUK_Compasser.domain.store.dto.req.StoreReqDTO;
import Comprehensive_Design_Project.CUK_Compasser.domain.store.entity.Store;
import Comprehensive_Design_Project.CUK_Compasser.domain.store.entity.Tag;
import Comprehensive_Design_Project.CUK_Compasser.domain.store.repository.StoreRepository;
import Comprehensive_Design_Project.CUK_Compasser.domain.storeManager.repository.StoreManagerRepository;
import Comprehensive_Design_Project.CUK_Compasser.global.common.apiPayload.code.status.ErrorStatus;
import Comprehensive_Design_Project.CUK_Compasser.global.common.apiPayload.exception.GeneralException;
import Comprehensive_Design_Project.CUK_Compasser.global.integration.kakao.kakao.dto.KakaoAddressSearchRespDTO;
import Comprehensive_Design_Project.CUK_Compasser.global.integration.kakao.kakao.service.KakaoLocalService;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    @Transactional
    public StoreRespDTO updateStore(Long storeId, Long memberId, StoreUpdateReqDTO req) {

        if (!storeManagerRepository.existsById(memberId)) {
            throw new GeneralException(ErrorStatus.STORE_MANAGER_NOT_FOUND);
        }

        Store store = storeRepository.findByIdAndStoreManager_MemberId(storeId, memberId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.STORE_FORBIDDEN));

        if (req.getStoreName() != null) store.setStoreName(req.getStoreName());

        if (req.getStoreEmail() != null) store.setStoreEmail(req.getStoreEmail());

        if (req.getBusinessHours() != null) {
            validateBusinessHours(req.getBusinessHours());
            store.setBusinessHours(storeConverter.toRawJson(req.getBusinessHours()));
        }

        return storeConverter.toResp(store);
    }

    @Transactional(readOnly = true)
    public StoreRespDTO getMyStore(Long storeId, Long memberId) {

        if (!storeManagerRepository.existsById(memberId)) {
            throw new GeneralException(ErrorStatus.STORE_MANAGER_NOT_FOUND);
        }

        Store store = storeRepository.findByIdAndStoreManager_MemberId(storeId, memberId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.STORE_FORBIDDEN));

        return storeConverter.toResp(store);
    }

    @Transactional
    public StoreRespDTO updateLocation(Long storeId, Long memberId, StoreLocationUpdateReqDTO req) {

        if (!storeManagerRepository.existsById(memberId)) {
            throw new GeneralException(ErrorStatus.STORE_MANAGER_NOT_FOUND);
        }

        Store store = storeRepository.findByIdAndStoreManager_MemberId(storeId, memberId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.STORE_FORBIDDEN));

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

    private void validateBusinessHours(JsonNode node) {
        if (node.get("weekly") == null) {
            throw new GeneralException(ErrorStatus.BUSINESS_HOURS_INVALID);
        }
    }

    @Transactional (readOnly = true)
    public List<StoreRespPagingDTO.GetStoreReqDTO> getStoreList(StoreReqDTO.StoreReqWithCoordinateDTO dto, int page) {
        List<Store> storeList = storeRepository.findStoresWithinRadius(dto.getLatitude(), dto.getLongitude(), 3, PageRequest.of(page, 10)).getContent();
//        List<Store> allByOrderByCreatedAtDesc = storeRepository.findAllByOrderByCreatedAtDesc(PageRequest.of(page, 10)).getContent();
        return storeConverter.toGetStoreDTOList(storeList);
    }

    @Transactional (readOnly = true)
    public List<StoreRespPagingDTO.GetStoreReqDTO> getStoreListByTag (int page, StoreReqDTO.StoreReqWithCoordinateAndTagDTO dto){
//        List<Store> allByTag = storeRepository.findAllByTagOrderByCreatedAtDesc(tag, PageRequest.of(page, 10)).getContent();
        List<Store> storeList = storeRepository.findStoresByTagWithinRadius(
                dto.getLatitude(),
                dto.getLongitude(),
                3,
                dto.getTag().toString(),
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
    public StoreRespDTO getStoreInfo (Long storeId){
        return storeConverter.toResp(storeRepository.findById(storeId).orElseThrow(() -> new GeneralException(ErrorStatus.STORE_NOT_FOUND)));
    }

}