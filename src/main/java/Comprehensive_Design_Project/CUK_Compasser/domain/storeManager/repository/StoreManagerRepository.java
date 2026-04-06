package Comprehensive_Design_Project.CUK_Compasser.domain.storeManager.repository;

import Comprehensive_Design_Project.CUK_Compasser.domain.reservation.entity.ReservationStatus;
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
            "from Member m " +
            "cross join Store s " +
            "join Reservation rs on rs.member = m and rs.store = s and rs.status = :status" + // 예약, 주문은 필수
            "left join Reward r on r.member = m and r.store = s " +    // 적립은 선택 (null 가능)
            "where m.id = :memberId and s.storeManager.id = :storeManagerId " +
            "and rs.status = :status")
    Optional<StoreManagerRespDTO.GetMemberRewardDTO> getMemberRewardByMemberIdAndStoreId(
            @Param("memberId") Long memberId,
            @Param("storeManagerId") Long storeManagerId,
            @Param("status") ReservationStatus status);
}