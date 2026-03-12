package Comprehensive_Design_Project.CUK_Compasser.domain.reward.service;

import Comprehensive_Design_Project.CUK_Compasser.domain.reward.dto.RewardSummary;
import Comprehensive_Design_Project.CUK_Compasser.domain.rewardHistory.entity.RewardType;
import Comprehensive_Design_Project.CUK_Compasser.domain.rewardHistory.repository.RewardHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RewardService {

    private final RewardHistoryRepository rewardHistoryRepository;


    /*
    * 사장님 홈 페이지에서 사용할 DTO!
    * */
    public RewardSummary getStoreRewardInToday (Long storeId){

        LocalDateTime start = LocalDate.now().atStartOfDay();
        LocalDateTime end = LocalDateTime.now();

        long earnCount = rewardHistoryRepository.countByStore_IdAndTypeAndCreatedAtBetween(storeId, RewardType.EARN, start, end);
        long couponCount = rewardHistoryRepository.countByStore_IdAndTypeAndCreatedAtBetween(storeId, RewardType.COUPON, start, end);

        return new RewardSummary(earnCount, couponCount);
    }
}

