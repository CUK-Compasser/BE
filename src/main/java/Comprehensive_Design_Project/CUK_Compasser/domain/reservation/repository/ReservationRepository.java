package Comprehensive_Design_Project.CUK_Compasser.domain.reservation.repository;

import Comprehensive_Design_Project.CUK_Compasser.domain.member.entity.Member;
import Comprehensive_Design_Project.CUK_Compasser.domain.reservation.entity.PickupStatus;
import Comprehensive_Design_Project.CUK_Compasser.domain.reservation.entity.Reservation;
import Comprehensive_Design_Project.CUK_Compasser.domain.reservation.entity.ReservationStatus;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

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

    @EntityGraph(attributePaths = {"store", "randomBox"})
    List<Reservation> findAllByMemberAndPickupStatusNotOrderByCreatedAtDesc(Member member, PickupStatus pickupStatus);

    @EntityGraph(attributePaths = {"store", "randomBox"})
    List<Reservation> findAllByMemberAndPickupStatusOrderByCreatedAtDesc(Member member, PickupStatus pickupStatus);

    boolean existsByRandomBox_IdAndMember_IdAndStatusIn(Long randomBoxId, Long memberId, List<ReservationStatus> statuses);

    /// ////////
    @EntityGraph(attributePaths = {
            "member",
            "store",
            "store.storeManager",
            "randomBox"
    })
    Optional<Reservation> findByIdAndMember(Long reservationId, Member member);
}