package Comprehensive_Design_Project.CUK_Compasser.domain.store.service;

import Comprehensive_Design_Project.CUK_Compasser.domain.member.dto.MemberReqDTO;
import Comprehensive_Design_Project.CUK_Compasser.domain.store.converter.StoreConverter;
import Comprehensive_Design_Project.CUK_Compasser.domain.store.dto.StoreLocationUpdateReqDTO;
import Comprehensive_Design_Project.CUK_Compasser.domain.store.dto.StoreRespDTO;
import Comprehensive_Design_Project.CUK_Compasser.domain.store.dto.StoreRespPagingDTO;
import Comprehensive_Design_Project.CUK_Compasser.domain.store.dto.StoreUpdateReqDTO;
import Comprehensive_Design_Project.CUK_Compasser.domain.store.entity.Store;
import Comprehensive_Design_Project.CUK_Compasser.domain.store.repository.StoreRepository;
import Comprehensive_Design_Project.CUK_Compasser.domain.storeManager.repository.StoreManagerRepository;
import Comprehensive_Design_Project.CUK_Compasser.global.common.apiPayload.code.status.ErrorStatus;
import Comprehensive_Design_Project.CUK_Compasser.global.common.apiPayload.exception.GeneralException;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;
    private final StoreManagerRepository storeManagerRepository;
    private final StoreConverter storeConverter;

    @Transactional
    public StoreRespDTO updateStore(Long storeId, Long memberId, StoreUpdateReqDTO req) {

        // ✅ 점장인지 확인 (PK=FK)
        if (!storeManagerRepository.existsById(memberId)) {
            throw new GeneralException(ErrorStatus.STORE_MANAGER_NOT_FOUND);
        }

        // ✅ 내 가게인지 권한 체크
        Store store = storeRepository.findByIdAndStoreManager_MemberId(storeId, memberId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.STORE_FORBIDDEN));

        if (req.getStoreName() != null) store.setStoreName(req.getStoreName());
        if (req.getStoreDetails() != null) store.setStoreDetails(req.getStoreDetails());
        if (req.getBeforePrice() != null) store.setBeforePrice(req.getBeforePrice());
        if (req.getAfterPrice() != null) store.setAfterPrice(req.getAfterPrice());

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

        if (req.getLatitude() == null || req.getLongitude() == null) {
            throw new GeneralException(ErrorStatus.INVALID_LOCATION);
        }

        store.setLatitude(req.getLatitude());
        store.setLongitude(req.getLongitude());

        return storeConverter.toResp(store);
    }

    private void validateBusinessHours(JsonNode node) {
        if (node.get("weekly") == null) {
            throw new GeneralException(ErrorStatus.BUSINESS_HOURS_INVALID);
        }
    }

    @Transactional (readOnly = true)
    public List<StoreRespPagingDTO.GetStoreOrderByCreatedDTO> getStoreList(int page) {
        Pageable pageable = PageRequest.of(page, 10);
        List<Store> allByOrderByCreatedAtDesc = storeRepository.findAllByOrderByCreatedAtDesc(pageable).getContent();
        return storeConverter.toGetStoreByCreatedDTO(allByOrderByCreatedAtDesc);

    }

    @Transactional (readOnly = true)
    public List<StoreRespDTO> getStoreListByTag (String email, String tag){

        return null;
    }

    @Transactional (readOnly = true)
    public List<StoreRespDTO> getStoreListByUniversity (String email, String university){

        return null;
    }

    @Transactional (readOnly = true)
    public List<StoreRespDTO> getStoreListByMemberRadius (String email, MemberReqDTO.MemberCoordinatesDTO coordinates){

        return null;
    }


}