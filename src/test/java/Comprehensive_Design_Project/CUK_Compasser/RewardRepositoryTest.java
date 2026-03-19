package Comprehensive_Design_Project.CUK_Compasser;

import Comprehensive_Design_Project.CUK_Compasser.domain.member.entity.Login;
import Comprehensive_Design_Project.CUK_Compasser.domain.member.entity.Member;
import Comprehensive_Design_Project.CUK_Compasser.domain.member.entity.MemberRole;
import Comprehensive_Design_Project.CUK_Compasser.domain.member.entity.Role;
import Comprehensive_Design_Project.CUK_Compasser.domain.member.repository.MemberRepository;
import Comprehensive_Design_Project.CUK_Compasser.domain.reward.dto.RewardSummary;
import Comprehensive_Design_Project.CUK_Compasser.domain.reward.entity.Reward;
import Comprehensive_Design_Project.CUK_Compasser.domain.reward.repository.RewardRepository;
import Comprehensive_Design_Project.CUK_Compasser.domain.store.entity.Store;
import Comprehensive_Design_Project.CUK_Compasser.domain.store.repository.StoreRepository;
import Comprehensive_Design_Project.CUK_Compasser.domain.storeManager.entity.StoreManager;
import Comprehensive_Design_Project.CUK_Compasser.domain.storeManager.repository.StoreManagerRepository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class RewardRepositoryTest {

    @Autowired
    private RewardRepository rewardRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private StoreManagerRepository storeManagerRepository;

    @Test
    @DisplayName("회원 ID로 리워드 요약 정보(스탬프 합계, 누적 사용 쿠폰 합계)를 한 번의 쿼리로 조회한다.")
    void getRewardSummaryByMemberIdTest() {
        // 1. given: 리워드를 적립받을 일반 회원 생성
        Member member = Member.builder()
                .memberName("테스터")
                .nickname("테스트닉네임")
                .email("test@test.com")
                .provider(Login.KAKAO)
                .providerId("123456789")
                .role(MemberRole.NORMAL)
                .status(Role.ACTIVE)
                .build();
        memberRepository.save(member);

        // 2. given: 상점 관리를 위한 사장님 회원 생성
        Member manager = Member.builder()
                .memberName("사장님")
                .nickname("사장님닉")
                .email("manager@test.com")
                .provider(Login.NORMAL)
                .role(MemberRole.STORE_MANAGER)
                .status(Role.ACTIVE)
                .build();
        memberRepository.save(manager);

        // 3. given: 상점 관리자 엔티티 생성
        StoreManager storeManager = StoreManager.builder()
                .member(manager)
                .businessLicenseNumber("123-45-67890")
                .build();
        storeManagerRepository.save(storeManager);

        // 4. given: 상점 엔티티 생성
        Store store = Store.builder()
                .storeManager(storeManager)
                .storeName("테스트 카페")
                .build();
        storeRepository.save(store);

        // 5. given: 리워드 생성 (Store 연관관계 및 coupon 필드 필수 추가!)
        Reward reward1 = Reward.builder()
                .member(member)
                .store(store)       // 💡 Store 연관관계 추가 (STORE_ID NOT NULL 에러 해결!)
                .stamp(5)           // 현재 스탬프 5개
                .coupon(1)          // 보유 쿠폰 1개
                .useCouponCnt(1)    // 누적 사용 쿠폰 1개
                .build();

        Reward reward2 = Reward.builder()
                .member(member)
                .store(store)       // 💡 Store 연관관계 추가
                .stamp(3)
                .coupon(0)
                .useCouponCnt(2)
                .build();

        rewardRepository.save(reward1);
        rewardRepository.save(reward2);

        // 6. when: 통계 조회
        RewardSummary summary = rewardRepository.getRewardSummaryByMemberId(member.getId());

        // 7. then: 검증
        assertThat(summary).isNotNull();
        assertThat(summary.earnCount()).isEqualTo(8L);    // 5 + 3 = 8
        assertThat(summary.couponCount()).isEqualTo(3L);  // 1 + 2 = 3
    }

    @Test
    @DisplayName("리워드 내역이 없는 회원의 경우 0을 반환한다.")
    void getRewardSummaryEmptyTest() {
        // given
        Member member = Member.builder()
                .memberName("신규회원")
                .nickname("뉴비")
                .email("new@test.com")
                .provider(Login.KAKAO)
                .providerId("987654321")
                .role(MemberRole.NORMAL)
                .status(Role.ACTIVE)
                .build();
        memberRepository.save(member);

        // when
        RewardSummary summary = rewardRepository.getRewardSummaryByMemberId(member.getId());

        // then
        assertThat(summary).isNotNull();
        assertThat(summary.earnCount()).isEqualTo(0L);
        assertThat(summary.couponCount()).isEqualTo(0L);
    }
}