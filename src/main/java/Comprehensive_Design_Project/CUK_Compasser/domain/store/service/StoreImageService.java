package Comprehensive_Design_Project.CUK_Compasser.domain.store.service;

import Comprehensive_Design_Project.CUK_Compasser.domain.store.converter.StoreImageConverter;
import Comprehensive_Design_Project.CUK_Compasser.domain.store.dto.resp.StoreImageListRespDTO;
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

import java.util.List;

@Service
@RequiredArgsConstructor
public class StoreImageService {

    private final StoreRepository storeRepository;
    private final StoreImageRepository storeImageRepository;
    private final StoreManagerRepository storeManagerRepository;
    private final StoreImageConverter storeImageConverter;

    @Transactional(readOnly = true)
    public StoreImageListRespDTO getImages(Long storeId, Long memberId) {
        assertOwner(storeId, memberId);

        List<StoreImageRespDTO> images = storeImageRepository.findAllByStore_IdOrderByCreatedAtAsc(storeId)
                .stream().map(storeImageConverter::toResp).toList();

        return StoreImageListRespDTO.builder()
                .storeId(storeId)
                .images(images)
                .build();
    }

    @Transactional
    public StoreImageListRespDTO uploadRepresentativeImage(Long storeId, Long memberId, MultipartFile storeImage) {
        Store store = assertOwner(storeId, memberId);

        if (storeImage == null || storeImage.isEmpty()) {
            throw new GeneralException(ErrorStatus.STORE_IMAGE_NOT_FOUND);
        }

        // ✅ (초기 구현) 실제 S3 업로드 전까지는 파일명을 URL처럼 저장 (TODO: S3 연동)
        String url = "uploaded://" + storeImage.getOriginalFilename();

        // 대표 1장 정책: 기존 이미지 삭제 후 1장 저장
        storeImageRepository.deleteAllByStore_Id(storeId);

        storeImageRepository.save(StoreImage.builder()
                .store(store)
                .imageUrl(url)
                .build());

        return getImages(storeId, memberId);
    }

    @Transactional
    public void deleteImage(Long storeId, Long imageId, Long memberId) {
        assertOwner(storeId, memberId);

        StoreImage image = storeImageRepository.findByIdAndStore_Id(imageId, storeId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.STORE_IMAGE_NOT_FOUND));

        storeImageRepository.delete(image);
    }

    private Store assertOwner(Long storeId, Long memberId) {
        if (!storeManagerRepository.existsById(memberId)) {
            throw new GeneralException(ErrorStatus.STORE_MANAGER_NOT_FOUND);
        }
        return storeRepository.findByIdAndStoreManager_MemberId(storeId, memberId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.STORE_FORBIDDEN));
    }
}