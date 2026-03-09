package Comprehensive_Design_Project.CUK_Compasser.domain.store.service;

import Comprehensive_Design_Project.CUK_Compasser.domain.store.converter.StoreImageConverter;
import Comprehensive_Design_Project.CUK_Compasser.domain.store.dto.StoreImageListRespDTO;
import Comprehensive_Design_Project.CUK_Compasser.domain.store.dto.StoreImageRespDTO;
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

import java.util.List;

@Service
@RequiredArgsConstructor
public class StoreImageService {

    private final StoreRepository storeRepository;
    private final StoreImageRepository storeImageRepository;
    private final StoreManagerRepository storeManagerRepository;
    private final StoreImageConverter storeImageConverter;

    @Transactional(readOnly = true)
    public StoreImageListRespDTO getImages(Long memberId) {
        Store store = getMyStoreEntity(memberId);

        List<StoreImageRespDTO> images = storeImageRepository.findAllByStore_IdOrderByCreatedAtAsc(store.getId())
                .stream()
                .map(storeImageConverter::toResp)
                .toList();

        return StoreImageListRespDTO.builder()
                .storeId(store.getId())
                .images(images)
                .build();
    }

    @Transactional
    public StoreImageListRespDTO uploadRepresentativeImage(Long memberId, MultipartFile storeImage) {
        Store store = getMyStoreEntity(memberId);

        if (storeImage == null || storeImage.isEmpty()) {
            throw new GeneralException(ErrorStatus.STORE_IMAGE_NOT_FOUND);
        }

        String url = "uploaded://" + storeImage.getOriginalFilename();

        // 대표 1장 정책
        storeImageRepository.deleteAllByStore_Id(store.getId());

        storeImageRepository.save(StoreImage.builder()
                .store(store)
                .imageUrl(url)
                .build());

        return getImages(memberId);
    }

    @Transactional
    public void deleteImage(Long imageId, Long memberId) {
        Store store = getMyStoreEntity(memberId);

        StoreImage image = storeImageRepository.findByIdAndStore_Id(imageId, store.getId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.STORE_IMAGE_NOT_FOUND));

        storeImageRepository.delete(image);
    }

    private Store getMyStoreEntity(Long memberId) {
        if (!storeManagerRepository.existsById(memberId)) {
            throw new GeneralException(ErrorStatus.STORE_MANAGER_NOT_FOUND);
        }

        return storeRepository.findByStoreManager_MemberId(memberId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.STORE_NOT_FOUND));
    }
}