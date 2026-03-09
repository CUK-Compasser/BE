package Comprehensive_Design_Project.CUK_Compasser.domain.reward.repository;

import Comprehensive_Design_Project.CUK_Compasser.domain.reward.entity.Reward;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RewardRepository extends JpaRepository<Reward, Long> {

    Optional<Reward> findByMember_IdAndStore_Id(Long memberId, Long storeId);
}
