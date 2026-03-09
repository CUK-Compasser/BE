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

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OwnerUpgradeServiceImpl implements OwnerUpgradeService {

    private static final String DEFAULT_STORE_NAME = "미등록 매장";
    private static final String DEFAULT_STORE_IMAGE_URL = "https://example.com/default-store.png";

    private final MemberRepository memberRepository;
    private final StoreManagerRepository storeManagerRepository;
    private final StoreRepository storeRepository;
    private final StoreImageRepository storeImageRepository;

    /**
     * ✅ (옵션) "승격만" 수행하는 기존 API용
     * - 이제는 사업자 검증이 되어있는지(verifiedAt) 체크하고 승격/생성 처리
     */
    @Override
    @Transactional
    public OwnerUpgradeRespDTO upgradeToStoreManager(Long memberId) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        StoreManager storeManager = storeManagerRepository.findByMember_Id(memberId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.BUSINESS_LICENSE_NOT_REGISTERED));

        if (isBlank(storeManager.getBusinessLicenseNumber()) || storeManager.getVerifiedAt() == null) {
            throw new GeneralException(ErrorStatus.BUSINESS_LICENSE_NOT_VERIFIED);
        }

        return upgradeAndProvision(member, storeManager);
    }

    /**
     * ✅ 화면용 원스텝: 사업자번호 검증 + 점장승격 + store/storeImage 자동 생성(멱등)
     */
    @Override
    @Transactional
    public OwnerUpgradeRespDTO verifyBusinessLicenseAndUpgrade(Long memberId, String businessLicenseNumber) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        String bizNo = normalizeBizNo(businessLicenseNumber);
        validateBizNoFormat(bizNo);

        // (선택) 외부 검증 붙일 자리
        // boolean valid = externalBizVerifier.verify(bizNo);
        // if (!valid) throw new GeneralException(ErrorStatus.BUSINESS_LICENSE_VERIFY_FAILED);

        // ✅ store_manager upsert (없으면 생성, 있으면 업데이트)
        StoreManager storeManager = storeManagerRepository.findByMember_Id(memberId)
                .orElseGet(() -> storeManagerRepository.save(
                        StoreManager.builder()
                                .member(member)
                                .businessLicenseNumber(bizNo)
                                .verifiedAt(LocalDateTime.now())
                                .build()
                ));

        // 정책: 이미 존재하면 번호/검증시간 갱신 허용 (원하면 "이미 등록됨"으로 막을 수도 있음)
        storeManager.setBusinessLicenseNumber(bizNo);
        storeManager.setVerifiedAt(LocalDateTime.now());

        return upgradeAndProvision(member, storeManager);
    }

    /**
     * ✅ 공통: role=STORE_MANAGER 승격 + store/storeImage 멱등 생성
     * - 이미 STORE_MANAGER면 alreadyUpgraded=true 반환
     */
    private OwnerUpgradeRespDTO upgradeAndProvision(Member member, StoreManager storeManager) {

        Long memberId = member.getId();

        boolean alreadyUpgraded = (member.getRole() == MemberRole.STORE_MANAGER);

        // ✅ role 승격(멱등)
        member.setRole(MemberRole.STORE_MANAGER);

        // ✅ store 멱등 생성
        Store store = storeRepository.findByStoreManager_MemberId(memberId)
                .orElseGet(() -> storeRepository.save(
                        Store.builder()
                                .storeManager(storeManager)
                                .storeName(DEFAULT_STORE_NAME)
                                .storeDetails(null)
                                .latitude(null)
                                .longitude(null)
                                .businessHours(null)
                                .build()
                ));

        return OwnerUpgradeRespDTO.builder()
                .memberId(memberId)
                .role(MemberRole.STORE_MANAGER)
                .storeId(store.getId())
                .alreadyUpgraded(alreadyUpgraded)
                .build();
    }

    private String normalizeBizNo(String bizNo) {
        if (bizNo == null) return null;
        return bizNo.replaceAll("\\D", ""); // 숫자만 남김
    }

    private void validateBizNoFormat(String bizNo) {
        if (bizNo == null) throw new GeneralException(ErrorStatus.BUSINESS_LICENSE_REQUIRED);
        if (bizNo.length() != 10) throw new GeneralException(ErrorStatus.BUSINESS_LICENSE_INVALID_FORMAT);
    }

    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}