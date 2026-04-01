package Comprehensive_Design_Project.CUK_Compasser.domain.reservation.repository;

import Comprehensive_Design_Project.CUK_Compasser.domain.member.entity.Member;
import Comprehensive_Design_Project.CUK_Compasser.domain.reservation.entity.PaymentStatus;
import Comprehensive_Design_Project.CUK_Compasser.domain.reservation.entity.Reservation;
import Comprehensive_Design_Project.CUK_Compasser.domain.reservation.entity.ReservationStatus;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @EntityGraph(attributePaths = {"member", "store", "randomBox"})
    List<Reservation> findAllByStore_IdAndStatusOrderByCreatedAtDesc(Long storeId, ReservationStatus status);

    @EntityGraph(attributePaths = {"member", "store", "randomBox"})
    List<Reservation> findAllByStore_IdAndStatusInOrderByCreatedAtDesc(Long storeId, List<ReservationStatus> statuses);

    @EntityGraph(attributePaths = {"member", "store", "randomBox"})
    Optional<Reservation> findByIdAndStore_Id(Long reservationId, Long storeId);

    @EntityGraph(attributePaths = {"member", "store", "randomBox"})
    Optional<Reservation> findById(Long reservationId);

    ///////////// 오더에 사용하는 레포
    @EntityGraph(attributePaths = {"store", "randomBox"})
    List<Reservation> findAllByMemberOrderByCreatedAtDesc(Member member);

    boolean existsByRandomBox_IdAndMember_IdAndStatusIn(Long randomBoxId, Long memberId, List<ReservationStatus> statuses);

    Optional<Reservation> findByIdAndMember_Id(Long reservationId, Long memberId);

    /// ////////
    @EntityGraph(attributePaths = {
            "member",
            "store",
            "store.storeManager",
            "randomBox"
    })
    Optional<Reservation> findByIdAndMember(Long reservationId, Member member);

    // =========================
    // Settlement
    // =========================

    /**
     * 정산 대상 조회
     * - 점장 본인 가게의 주문 중
     *   APPROVED + PAID + settled = false 인 주문만 조회한다.
     */
    @EntityGraph(attributePaths = {
            "member",
            "store",
            "store.storeManager",
            "randomBox"
    })
    List<Reservation> findAllByStore_IdAndStatusAndPaymentStatusAndSettledFalseOrderByCreatedAtAsc(
            Long storeId,
            ReservationStatus status,
            PaymentStatus paymentStatus
    );

    /**
     * 정산 완료 처리 대상 조회
     * - 요청으로 들어온 reservationIds 중
     *   해당 점장 가게에 속한 주문만 조회한다.
     */
    @EntityGraph(attributePaths = {
            "member",
            "store",
            "store.storeManager",
            "randomBox"
    })
    List<Reservation> findAllByIdInAndStore_Id(List<Long> reservationIds, Long storeId);

    @Query("select r from Reservation r join fetch r.randomBox where r.member.id = :memberId and r.store.id = :storeId " +
            "and r.status = :status order by r.createdAt desc limit 1")
    Reservation findByMember_IdAndStore_IdAndStatus(@Param("memberId") Long memberId,
                                                    @Param("storeId") Long storeId,
                                                    @Param("status") ReservationStatus status);
}