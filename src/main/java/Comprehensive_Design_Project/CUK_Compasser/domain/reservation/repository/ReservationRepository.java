package Comprehensive_Design_Project.CUK_Compasser.domain.reservation.repository;

import Comprehensive_Design_Project.CUK_Compasser.domain.reservation.entity.Reservation;
import Comprehensive_Design_Project.CUK_Compasser.domain.reservation.entity.ReservationStatus;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @EntityGraph(attributePaths = {"member", "store"})
    List<Reservation> findAllByStore_IdAndStatusOrderByCreatedAtDesc(Long storeId, ReservationStatus status);

    @EntityGraph(attributePaths = {"member", "store"})
    List<Reservation> findAllByStore_IdAndStatusInOrderByCreatedAtDesc(Long storeId, List<ReservationStatus> statuses);

    @EntityGraph(attributePaths = {"member", "store"})
    Optional<Reservation> findByIdAndStore_Id(Long reservationId, Long storeId);

    @EntityGraph(attributePaths = {"member", "store"})
    Optional<Reservation> findById(Long reservationId);
}