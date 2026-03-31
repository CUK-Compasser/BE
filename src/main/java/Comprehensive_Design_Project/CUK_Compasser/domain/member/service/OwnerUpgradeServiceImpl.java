package Comprehensive_Design_Project.CUK_Compasser.domain.member.service;

import Comprehensive_Design_Project.CUK_Compasser.domain.member.dto.OwnerBusinessVerifyReqDTO;
import Comprehensive_Design_Project.CUK_Compasser.domain.member.dto.OwnerUpgradeRespDTO;
import Comprehensive_Design_Project.CUK_Compasser.domain.member.entity.Member;
import Comprehensive_Design_Project.CUK_Compasser.domain.member.entity.MemberRole;
import Comprehensive_Design_Project.CUK_Compasser.domain.member.repository.MemberRepository;
import Comprehensive_Design_Project.CUK_Compasser.domain.store.entity.Store;
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

    private final MemberRepository memberRepository;
    private final StoreManagerRepository storeManagerRepository;
    private final StoreRepository storeRepository;

    // 추후 국세청 진위확인 클라이언트 주입
    // private final NtsBusinessVerifyClient ntsBusinessVerifyClient;

    /**
     * 기존 승격 API
     * - 이미 사업자 검증이 완료된 사용자만 점장 승격 가능
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
     * 사업자 진위확인 + 점장 승격
     * - 사업자등록번호, 대표자명, 개업일자를 이용해 외부 API 검증 후 승격 처리
     */
    @Override
    @Transactional
    public OwnerUpgradeRespDTO verifyBusinessLicenseAndUpgrade(Long memberId, OwnerBusinessVerifyReqDTO request) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        String bizNo = normalizeBizNo(request.getBusinessLicenseNumber());
        validateBizNoFormat(bizNo);
        validateStartDateFormat(request.getStartDate());
        validateOwnerName(request.getOwnerName());

        // 실제 국세청 진위확인 API 연동 시 사용
        // boolean valid = ntsBusinessVerifyClient.verify(
        //         bizNo,
        //         request.getStartDate(),
        //         request.getOwnerName(),
        //         request.getBusinessName()
        // );
        //
        // if (!valid) {
        //     throw new GeneralException(ErrorStatus.BUSINESS_LICENSE_VERIFY_FAILED);
        // }

        // store_manager upsert
        StoreManager storeManager = storeManagerRepository.findByMember_Id(memberId)
                .orElseGet(() -> storeManagerRepository.save(
                        StoreManager.builder()
                                .member(member)
                                .businessLicenseNumber(bizNo)
                                .verifiedAt(LocalDateTime.now())
                                .build()
                ));

        storeManager.setBusinessLicenseNumber(bizNo);
        storeManager.setVerifiedAt(LocalDateTime.now());

        return upgradeAndProvision(member, storeManager);
    }

    /**
     * 공통 처리
     * - 점장 권한 승격
     * - 매장 없으면 기본 매장 생성
     */
    private OwnerUpgradeRespDTO upgradeAndProvision(Member member, StoreManager storeManager) {

        Long memberId = member.getId();
        boolean alreadyUpgraded = (member.getRole() == MemberRole.STORE_MANAGER);

        member.setRole(MemberRole.STORE_MANAGER);

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

    /**
     * 사업자등록번호 숫자만 남기기
     */
    private String normalizeBizNo(String bizNo) {
        if (bizNo == null) return null;
        return bizNo.replaceAll("\\D", "");
    }

    /**
     * 사업자등록번호 형식 검증
     */
    private void validateBizNoFormat(String bizNo) {
        if (bizNo == null) {
            throw new GeneralException(ErrorStatus.BUSINESS_LICENSE_REQUIRED);
        }
        if (bizNo.length() != 10) {
            throw new GeneralException(ErrorStatus.BUSINESS_LICENSE_INVALID_FORMAT);
        }
    }

    /**
     * 개업일자 형식 검증
     * - yyyyMMdd 8자리 숫자
     */
    private void validateStartDateFormat(String startDate) {
        if (isBlank(startDate)) {
            throw new GeneralException(ErrorStatus.BUSINESS_OPEN_DATE_REQUIRED);
        }
        if (!startDate.matches("^\\d{8}$")) {
            throw new GeneralException(ErrorStatus.BUSINESS_OPEN_DATE_INVALID_FORMAT);
        }
    }

    /**
     * 대표자명 검증
     */
    private void validateOwnerName(String ownerName) {
        if (isBlank(ownerName)) {
            throw new GeneralException(ErrorStatus.OWNER_NAME_REQUIRED);
        }
    }

    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}