package Comprehensive_Design_Project.CUK_Compasser.domain.reward.repository;

import Comprehensive_Design_Project.CUK_Compasser.domain.reward.dto.SummaryMemberReward;
import Comprehensive_Design_Project.CUK_Compasser.domain.reward.entity.Reward;
import Comprehensive_Design_Project.CUK_Compasser.domain.storeManager.dto.resp.RewardRespRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RewardRepository extends JpaRepository<Reward, Long> {

    Optional<Reward> findByMember_IdAndStore_Id(Long memberId, Long storeId);

    @Query("select r from Reward r join fetch Store s where r.member.id = :memberId")
    List<Reward> findAllByMember_Id(Long memberId);

    /*@Query("SELECT new Comprehensive_Design_Project.CUK_Compasser.domain.storeManager.dto.resp.RewardRespRecord(" +
            "m.id, m.nickname, s.id, r.id, r.stamp, r.coupon, r.createdAt) " +
            "FROM Member m " +
            "CROSS JOIN Store s " + // 명시
            "LEFT JOIN Reward r ON r.member = m AND r.store = s " +
            "WHERE m.id = :memberId AND s.storeManager.id = :storeManagerId")
    Optional<RewardRespRecord> findRewardRecord(
            @Param("memberId") Long memberId,
            @Param("storeManagerId") Long storeManagerId
    );*/

    @Query("SELECT new Comprehensive_Design_Project.CUK_Compasser.domain.reward.dto.SummaryMemberReward(" +
            "COALESCE(SUM(r.stamp), 0), " +
            "COALESCE(SUM(r.useCouponCnt), 0)) " +
            "FROM Reward r " +
            "WHERE r.member.id = :memberId")
    SummaryMemberReward findTotalRewardSummaryByMemberId(@Param("memberId") Long memberId);

}
