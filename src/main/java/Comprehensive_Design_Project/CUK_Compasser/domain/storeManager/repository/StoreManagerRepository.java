package Comprehensive_Design_Project.CUK_Compasser.domain.storeManager.repository;

import Comprehensive_Design_Project.CUK_Compasser.domain.storeManager.dto.resp.StoreManagerRespDTO;
import Comprehensive_Design_Project.CUK_Compasser.domain.storeManager.entity.StoreManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface StoreManagerRepository extends JpaRepository<StoreManager, Long> {

    // memberId로 점장 조회 (연관관계 기준)
    Optional<StoreManager> findByMember_Id(Long memberId);

    // 점장 여부 체크
    boolean existsByMember_Id(Long memberId);

    /*
    * member, store, reservation = unique, not null,
    * reward = could be null
    * */
    @Query("select new Comprehensive_Design_Project.CUK_Compasser.domain.storeManager.dto.resp.StoreManagerRespDTO.GetMemberRewardDTO(" +
            "r.id, " +
            "s.id, " +
            "m.id, m.nickname, m.email, " +
            "rs.randomBox.boxName, rs.totalPrice, " +
            "r.stamp, r.coupon) " +
            "FROM Member m " +
            "CROSS JOIN Store s " +
            "JOIN Reservation rs ON rs.member = m AND rs.store = s " + // 예약, 주문은 필수
            "LEFT JOIN Reward r ON r.member = m AND r.store = s " +    // 적립은 선택 (null 가능)
            "WHERE m.id = :memberId AND s.storeManager.id = :storeManagerId")
    Optional<StoreManagerRespDTO.GetMemberRewardDTO> getMemberRewardByMemberIdAndStoreId(
            @Param("memberId") Long memberId,
            @Param("storeManagerId") Long storeManagerId);
}