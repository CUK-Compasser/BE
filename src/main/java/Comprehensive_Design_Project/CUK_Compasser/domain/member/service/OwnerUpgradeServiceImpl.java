package Comprehensive_Design_Project.CUK_Compasser.domain.member.service;

import Comprehensive_Design_Project.CUK_Compasser.domain.member.dto.OwnerUpgradeRespDTO;
import Comprehensive_Design_Project.CUK_Compasser.domain.member.entity.Member;
import Comprehensive_Design_Project.CUK_Compasser.domain.member.entity.MemberRole;
import Comprehensive_Design_Project.CUK_Compasser.domain.member.repository.MemberRepository;
import Comprehensive_Design_Project.CUK_Compasser.domain.store.entity.Store;
import Comprehensive_Design_Project.CUK_Compasser.domain.store.entity.StoreImage;
import Comprehensive_Design_Project.CUK_Compasser.domain.store.repository.StoreImageRepository;
import Comprehensive_Design_Project.CUK_Compasser.domain.store.repository.StoreRepository;
import Comprehensive_Design_Project.CUK_Compasser.domain.storeManager.entity.StoreManager;
import Comprehensive_Design_Project.CUK_Compasser.domain.storeManager.repository.StoreManagerRepository;
import Comprehensive_Design_Project.CUK_Compasser.global.common.apiPayload.code.status.ErrorStatus;
import Comprehensive_Design_Project.CUK_Compasser.global.common.apiPayload.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OwnerUpgradeServiceImpl implements OwnerUpgradeService {

    private static final String DEFAULT_STORE_NAME = "미등록 매장";
    private static final String DEFAULT_BIZ_LICENSE = "UNVERIFIED";

    // ✅ 기본 이미지 URL(정책값) - 나중에 실제 기본 이미지 경로로 교체
    private static final String DEFAULT_STORE_IMAGE_URL = "https://example.com/default-store.png";

    private final MemberRepository memberRepository;
    private final StoreManagerRepository storeManagerRepository;
    private final StoreRepository storeRepository;
    private final StoreImageRepository storeImageRepository;

    @Override
    @Transactional
    public OwnerUpgradeRespDTO upgradeToStoreManager(Long memberId) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        // ✅ (1) 이미 점장인 경우(멱등)
        if (member.getRole() == MemberRole.STORE_MANAGER) {
            Long storeId = storeRepository.findByStoreManager_MemberId(memberId)
                    .map(Store::getId)
                    .orElse(null);

            // (선택) 이미 점장인데 store가 존재하면, 이미지도 없을 때 기본이미지 보정
            if (storeId != null && !storeImageRepository.existsByStore_Id(storeId)) {
                Store store = storeRepository.findById(storeId)
                        .orElseThrow(() -> new GeneralException(ErrorStatus.STORE_NOT_FOUND));

                storeImageRepository.save(StoreImage.builder()
                        .store(store)
                        .imageUrl(DEFAULT_STORE_IMAGE_URL)
                        .build());
            }

            return OwnerUpgradeRespDTO.builder()
                    .memberId(memberId)
                    .role(MemberRole.STORE_MANAGER)
                    .storeId(storeId)
                    .alreadyUpgraded(true)
                    .build();
        }

        // ✅ (2) role 변경
        member.setRole(MemberRole.STORE_MANAGER);

        // ✅ (3) store_managers 생성(멱등)
        if (!storeManagerRepository.existsById(memberId)) {
            StoreManager storeManager = StoreManager.builder()
                    .memberId(memberId)
                    .businessLicenseNumber(DEFAULT_BIZ_LICENSE)
                    .verifiedAt(null)
                    .build();
            storeManagerRepository.save(storeManager);
        }

        // ✅ (4) stores 생성(멱등)
        Store store = storeRepository.findByStoreManager_MemberId(memberId)
                .orElseGet(() -> {
                    StoreManager manager = storeManagerRepository.findById(memberId)
                            .orElseThrow(() -> new GeneralException(ErrorStatus.STORE_MANAGER_NOT_FOUND));

                    return storeRepository.save(
                            Store.builder()
                                    .storeManager(manager)
                                    .storeName(DEFAULT_STORE_NAME)
                                    .storeDetails(null)
                                    .beforePrice(null)
                                    .afterPrice(null)
                                    .latitude(null)
                                    .longitude(null)
                                    .businessHours(null)
                                    .build()
                    );
                });

        // ✅ (5) store_images 기본 1장 생성(멱등)
        if (!storeImageRepository.existsByStore_Id(store.getId())) {
            storeImageRepository.save(StoreImage.builder()
                    .store(store)
                    .imageUrl(DEFAULT_STORE_IMAGE_URL)
                    .build());
        }

        return OwnerUpgradeRespDTO.builder()
                .memberId(memberId)
                .role(MemberRole.STORE_MANAGER)
                .storeId(store.getId())
                .alreadyUpgraded(false)
                .build();
    }
}