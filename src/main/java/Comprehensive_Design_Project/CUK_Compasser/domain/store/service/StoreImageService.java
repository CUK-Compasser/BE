package Comprehensive_Design_Project.CUK_Compasser.domain.store.service;

import Comprehensive_Design_Project.CUK_Compasser.domain.store.converter.StoreImageConverter;
import Comprehensive_Design_Project.CUK_Compasser.domain.store.dto.resp.StoreImageRespDTO;
import Comprehensive_Design_Project.CUK_Compasser.domain.store.entity.Store;
import Comprehensive_Design_Project.CUK_Compasser.domain.store.entity.StoreImage;
import Comprehensive_Design_Project.CUK_Compasser.domain.store.repository.StoreImageRepository;
import Comprehensive_Design_Project.CUK_Compasser.domain.store.repository.StoreRepository;
import Comprehensive_Design_Project.CUK_Compasser.domain.storeManager.repository.StoreManagerRepository;
import Comprehensive_Design_Project.CUK_Compasser.global.common.apiPayload.code.status.ErrorStatus;
import Comprehensive_Design_Project.CUK_Compasser.global.common.apiPayload.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class StoreImageService {

    private static final String DEFAULT_STORE_IMAGE_URL = "https://example.com/default-store.png";

    private final StoreRepository storeRepository;
    private final StoreImageRepository storeImageRepository;
    private final StoreManagerRepository storeManagerRepository;
    private final StoreImageConverter storeImageConverter;

    @Transactional(readOnly = true)
    public StoreImageRespDTO getRepresentativeImage(Long memberId) {
        Store store = getMyStoreEntity(memberId);

        return storeImageRepository.findFirstByStore_IdOrderByCreatedAtAsc(store.getId())
                .map(storeImageConverter::toResp)
                .orElseGet(() -> storeImageConverter.toDefaultResp(store.getId(), DEFAULT_STORE_IMAGE_URL));
    }

    @Transactional
    public StoreImageRespDTO uploadRepresentativeImage(Long memberId, MultipartFile storeImage) {
        Store store = getMyStoreEntity(memberId);

        if (storeImage == null || storeImage.isEmpty()) {
            throw new GeneralException(ErrorStatus.STORE_IMAGE_NOT_FOUND);
        }

        String url = "uploaded://" + storeImage.getOriginalFilename();

        storeImageRepository.deleteAllByStore_Id(store.getId());

        StoreImage saved = storeImageRepository.save(StoreImage.builder()
                .store(store)
                .imageUrl(url)
                .build());

        return storeImageConverter.toResp(saved);
    }

    @Transactional
    public void deleteRepresentativeImage(Long memberId) {
        Store store = getMyStoreEntity(memberId);

        storeImageRepository.findFirstByStore_IdOrderByCreatedAtAsc(store.getId())
                .ifPresent(storeImageRepository::delete);
    }

    private Store getMyStoreEntity(Long memberId) {
        if (!storeManagerRepository.existsById(memberId)) {
            throw new GeneralException(ErrorStatus.STORE_MANAGER_NOT_FOUND);
        }

        return storeRepository.findByStoreManager_MemberId(memberId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.STORE_NOT_FOUND));
    }
}