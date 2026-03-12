package Comprehensive_Design_Project.CUK_Compasser.domain.reservation.service;

import Comprehensive_Design_Project.CUK_Compasser.domain.reservation.converter.ReservationConverter;
import Comprehensive_Design_Project.CUK_Compasser.domain.reservation.dto.ReservationReqDTO;
import Comprehensive_Design_Project.CUK_Compasser.domain.reservation.dto.ReservationRespDTO;
import Comprehensive_Design_Project.CUK_Compasser.domain.reservation.entity.Reservation;
import Comprehensive_Design_Project.CUK_Compasser.domain.reservation.entity.ReservationStatus;
import Comprehensive_Design_Project.CUK_Compasser.domain.reservation.repository.ReservationRepository;
import Comprehensive_Design_Project.CUK_Compasser.global.common.apiPayload.code.status.ErrorStatus;
import Comprehensive_Design_Project.CUK_Compasser.global.common.apiPayload.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationConverter reservationConverter;

    /**
     * 예약 페이지 목록 조회
     *
     * - 예약 페이지에는 확인 대기중인 예약만 보여준다.
     * - 즉 REQUESTED 상태의 예약만 조회한다.
     *
     * 주의:
     * - 이 메서드는 현재 로그인한 점장의 가게 예약만 조회된다는 전제를 가진다.
     * - memberId를 직접 사용하지 않는 구조라면, 로그인단/상위 계층에서 이미 필터링된 상태여야 한다.
     */
    @Override
    public ReservationRespDTO.ReservationListDTO getPendingReservations(Long memberId) {
        List<Reservation> reservations =
                reservationRepository.findAllByStore_IdAndStatusOrderByCreatedAtDesc(
                        memberId, ReservationStatus.REQUESTED
                );

        return reservationConverter.toReservationListDTO(reservations);
    }

    /**
     * 주문 페이지 목록 조회
     *
     * - 주문 페이지에는 처리 완료된 예약만 보여준다.
     * - 즉 APPROVED, REJECTED, CANCELED 상태를 조회한다.
     *
     * 주의:
     * - 현재 구조상 memberId가 실제로 storeId 역할을 하도록 쓰면 안 된다.
     * - 이 메서드를 그대로 쓰려면 상위에서 실제 storeId를 넘겨주도록 다시 정리하는 것이 가장 안전하다.
     */
    @Override
    public ReservationRespDTO.ReservationListDTO getProcessedReservations(Long memberId) {
        List<ReservationStatus> processedStatuses = List.of(
                ReservationStatus.APPROVED,
                ReservationStatus.REJECTED,
                ReservationStatus.CANCELED
        );

        List<Reservation> reservations =
                reservationRepository.findAllByStore_IdAndStatusInOrderByCreatedAtDesc(
                        memberId, processedStatuses
                );

        return reservationConverter.toReservationListDTO(reservations);
    }

    /**
     * 예약 수락
     *
     * - REQUESTED 상태의 예약만 APPROVED로 변경할 수 있다.
     * - 수락 시 rejectReason은 null로 초기화한다.
     */
    @Override
    @Transactional
    public ReservationRespDTO.ReservationDTO approveReservation(Long reservationId, Long memberId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.RESERVATION_NOT_FOUND));

        if (reservation.getStatus() != ReservationStatus.REQUESTED) {
            throw new GeneralException(ErrorStatus.RESERVATION_ALREADY_PROCESSED);
        }

        reservation.setStatus(ReservationStatus.APPROVED);
        reservation.setRejectReason(null);

        return reservationConverter.toReservationDTO(reservation);
    }

    /**
     * 예약 거절
     *
     * - request.status 값이 반드시 있어야 한다.
     * - 현재 예약은 REQUESTED 상태일 때만 처리할 수 있다.
     * - APPROVED면 수락 처리
     * - REJECTED면 거절 처리하며 rejectReason은 필수다.
     * - 그 외 상태값은 점장 처리용 상태가 아니므로 예외 처리한다.
     */
    @Override
    @Transactional
    public ReservationRespDTO.ReservationDTO rejectReservation(Long reservationId, Long memberId,
                                                               ReservationReqDTO request) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.RESERVATION_NOT_FOUND));

        if (request.getStatus() == null) {
            throw new GeneralException(ErrorStatus.RESERVATION_STATUS_REQUIRED);
        }

        if (reservation.getStatus() != ReservationStatus.REQUESTED) {
            throw new GeneralException(ErrorStatus.RESERVATION_ALREADY_PROCESSED);
        }

        if (request.getStatus() == ReservationStatus.APPROVED) {
            reservation.setStatus(ReservationStatus.APPROVED);
            reservation.setRejectReason(null);

        } else if (request.getStatus() == ReservationStatus.REJECTED) {
            if (!StringUtils.hasText(request.getRejectReason())) {
                throw new GeneralException(ErrorStatus.REJECT_REASON_REQUIRED);
            }
            reservation.setStatus(ReservationStatus.REJECTED);
            reservation.setRejectReason(request.getRejectReason().trim());

        } else {
            throw new GeneralException(ErrorStatus.INVALID_RESERVATION_STATUS);
        }

        return reservationConverter.toReservationDTO(reservation);
    }
}