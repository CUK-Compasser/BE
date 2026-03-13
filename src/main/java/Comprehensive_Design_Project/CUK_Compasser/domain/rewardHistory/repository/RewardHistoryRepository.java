package Comprehensive_Design_Project.CUK_Compasser.domain.rewardHistory.repository;

import Comprehensive_Design_Project.CUK_Compasser.domain.rewardHistory.entity.RewardHistory;
import Comprehensive_Design_Project.CUK_Compasser.domain.rewardHistory.entity.RewardType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface RewardHistoryRepository extends JpaRepository<RewardHistory, Long> {
    long countByStore_IdAndTypeAndCreatedAtBetween(Long storeId, RewardType type, LocalDateTime start, LocalDateTime end);
}
