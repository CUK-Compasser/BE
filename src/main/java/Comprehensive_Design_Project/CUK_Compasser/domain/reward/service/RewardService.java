package Comprehensive_Design_Project.CUK_Compasser.domain.reward.service;

import Comprehensive_Design_Project.CUK_Compasser.domain.reward.dto.RewardSummary;
import Comprehensive_Design_Project.CUK_Compasser.domain.reward.dto.SummaryMemberReward;
import Comprehensive_Design_Project.CUK_Compasser.domain.reward.repository.RewardRepository;
import Comprehensive_Design_Project.CUK_Compasser.domain.rewardHistory.entity.RewardType;
import Comprehensive_Design_Project.CUK_Compasser.domain.rewardHistory.repository.RewardHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RewardService {

    private final RewardHistoryRepository rewardHistoryRepository;
    private final RewardRepository rewardRepository;


    /*
    * 사장님 홈 페이지에서 사용할 DTO!
    * */

    /**
     * 사장님 홈페이지에서 사용항 DTO 반환 Method
     * @author jijysun
     * @param storeId
     * @return domain.reward.dto.RewardSummary
     */
    @Transactional(readOnly = true)
    public RewardSummary getStoreRewardInToday (Long storeId){

        LocalDateTime start = LocalDate.now().atStartOfDay();
        LocalDateTime end = LocalDateTime.now();

        long earnCount = rewardHistoryRepository.countByStore_IdAndTypeAndCreatedAtBetween(storeId, RewardType.EARN, start, end);
        long couponCount = rewardHistoryRepository.countByStore_IdAndTypeAndCreatedAtBetween(storeId, RewardType.COUPON, start, end);

        return new RewardSummary(earnCount, couponCount);
    }


    /**
     * 사용자 마이페이제서 사용할 DTO 반환 Method
     * Order 변경 후 사용해주시면 됩니다.
     * @author jijysun
     * @param memberId
     * @return domain.reward.dto.SummaryMemberReward
     */
    @Transactional(readOnly = true)
    public SummaryMemberReward summaryMemberReward(Long memberId) {
        // reward -> select sum (r.stamp), sum (r.useCouponCnt) from Reward r where r.member.id = :memberId
        SummaryMemberReward rewardSummaryByMemberId = rewardRepository.findTotalRewardSummaryByMemberId(memberId);

        // memberId -> select count(o) from Order o where o.member.id = :memberId And o.orderStatus = 'PICKED_UP'
        Long unboxingCnt = 1L;

        return rewardSummaryByMemberId.withUnboxedCount(unboxingCnt);
    }

}

