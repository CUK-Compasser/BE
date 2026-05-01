package Comprehensive_Design_Project.CUK_Compasser.domain.reward.repository;

import Comprehensive_Design_Project.CUK_Compasser.domain.reward.dto.RewardEachStoreRecord;
import Comprehensive_Design_Project.CUK_Compasser.domain.reward.dto.RewardSummary;
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


    @Query("SELECT new Comprehensive_Design_Project.CUK_Compasser.domain.reward.dto.RewardSummary(" +
            "COALESCE(SUM(r.stamp), 0L), " +
            "COALESCE(SUM(r.useCouponCnt), 0L)) " +
            "FROM Reward r WHERE r.member.id = :memberId")
    RewardSummary getRewardSummaryByMemberId(@Param("memberId") Long memberId);

    @Query("select new Comprehensive_Design_Project.CUK_Compasser.domain.reward.dto.RewardEachStoreRecord( " +
            "r.store.storeName," +
            "r.stamp," +
            "r.coupon, " +
            "r.useCouponCnt)" +
            "from Reward r " +
            "where r.member.id = :memberId")
    List<RewardEachStoreRecord> findAllByMember_Id(@Param("memberId") Long memberId);

    @Query("SELECT new Comprehensive_Design_Project.CUK_Compasser.domain.reward.dto.SummaryMemberReward(" +
            "COALESCE(SUM(r.stamp), 0), " +
            "COALESCE(SUM(r.useCouponCnt), 0)) " +
            "FROM Reward r " +
            "WHERE r.member.id = :memberId")
    SummaryMemberReward findTotalRewardSummaryByMemberId(@Param("memberId") Long memberId);

}
