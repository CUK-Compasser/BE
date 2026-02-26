package Comprehensive_Design_Project.CUK_Compasser.domain.member.service;

import Comprehensive_Design_Project.CUK_Compasser.domain.member.dto.OwnerUpgradeRespDTO;
import Comprehensive_Design_Project.CUK_Compasser.domain.member.entity.Member;
import Comprehensive_Design_Project.CUK_Compasser.domain.member.entity.MemberRole;
import Comprehensive_Design_Project.CUK_Compasser.domain.member.repository.MemberRepository;
import Comprehensive_Design_Project.CUK_Compasser.domain.store.entity.Store;
import Comprehensive_Design_Project.CUK_Compasser.domain.store.repository.StoreRepository;
import Comprehensive_Design_Project.CUK_Compasser.domain.storeManager.entity.StoreManager;
import Comprehensive_Design_Project.CUK_Compasser.domain.storeManager.repository.StoreManagerRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OwnerUpgradeServiceImpl implements OwnerUpgradeService {

    private static final String DEFAULT_STORE_NAME = "미등록 매장";   // stores.store_name NOT NULL 대응
    private static final String DEFAULT_BIZ_LICENSE = "UNVERIFIED";  // store_managers.business_license_number NOT NULL 대응(정책값)

    private final MemberRepository memberRepository;
    private final StoreManagerRepository storeManagerRepository;
    private final StoreRepository storeRepository;

    @Override
    @Transactional
    public OwnerUpgradeRespDTO upgradeToStoreManager(Long memberId) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다. memberId=" + memberId));

        // ✅ 멱등: 이미 STORE_MANAGER면 storeId만 찾아서 반환
        if (member.getRole() == MemberRole.STORE_MANAGER) {
            Long storeId = storeRepository.findByStoreManagerId(memberId)
                    .map(Store::getId)
                    .orElse(null);

            return OwnerUpgradeRespDTO.builder()
                    .memberId(memberId)
                    .role(MemberRole.STORE_MANAGER)
                    .storeId(storeId)
                    .alreadyUpgraded(true)
                    .build();
        }

        // 1) role 변경
        member.setRole(MemberRole.STORE_MANAGER);

        // 2) store_managers 생성 (PK=FK=memberId)
        if (!storeManagerRepository.existsById(memberId)) {
            StoreManager storeManager = StoreManager.builder()
                    .memberId(memberId)
                    .businessLicenseNumber(DEFAULT_BIZ_LICENSE)
                    .verifiedAt(null)
                    .build();
            storeManagerRepository.save(storeManager);
        }

        // 3) stores 생성 (사장 1명당 1개면 UNIQUE(store_manager_id) 권장)
        StoreManager storeManager = storeManagerRepository.findById(memberId)
                .orElseThrow(() -> new IllegalStateException("storeManager 생성 후 조회 실패"));

        Store store = Store.builder()
                .storeManager(storeManager)     // ✅ 연관관계 주입
                .storeName("미등록 매장")
                .storeDetails(null)
                .beforePrice(null)
                .afterPrice(null)
                .latitude(null)
                .longitude(null)
                .businessHours(null)
                .build();
        storeRepository.save(store);

        // 4) store_images는 승격 시점 생성 X (업로드 때 생성)

        return OwnerUpgradeRespDTO.builder()
                .memberId(memberId)
                .role(MemberRole.STORE_MANAGER)
                .storeId(store.getId())
                .alreadyUpgraded(false)
                .build();
    }
}