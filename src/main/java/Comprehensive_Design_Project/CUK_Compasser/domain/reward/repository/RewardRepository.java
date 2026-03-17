package Comprehensive_Design_Project.CUK_Compasser.domain.reward.repository;

import Comprehensive_Design_Project.CUK_Compasser.domain.reward.entity.Reward;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RewardRepository extends JpaRepository<Reward, Long> {

    Optional<Reward> findByMember_IdAndStore_Id(Long memberId, Long storeId);

    @Query("select r from Reward r join fetch r.store s where r.member.id = :memberId")
    List<Reward> findAllByMember_Id(@Param("memberId") Long memberId);

    @Query("SELECT COALESCE(SUM(r.stamp), 0) FROM Reward r WHERE r.member.id = :memberId")
    Integer sumTotalStampsByMemberId(@Param("memberId") Long memberId);

    @Query("SELECT COALESCE(SUM(r.useCouponCnt), 0) FROM Reward r WHERE r.member.id = :memberId")
    Integer sumTotalUsedCouponsByMemberId(@Param("memberId") Long memberId);
}