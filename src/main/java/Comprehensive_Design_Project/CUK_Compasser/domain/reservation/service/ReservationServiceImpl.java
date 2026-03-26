package Comprehensive_Design_Project.CUK_Compasser.domain.reservation.service;

import Comprehensive_Design_Project.CUK_Compasser.domain.randomBox.entity.RandomBox;
import Comprehensive_Design_Project.CUK_Compasser.domain.randomBox.repository.RandomBoxRepository;
import Comprehensive_Design_Project.CUK_Compasser.domain.reservation.converter.ReservationConverter;
import Comprehensive_Design_Project.CUK_Compasser.domain.reservation.dto.ReservationReqDTO;
import Comprehensive_Design_Project.CUK_Compasser.domain.reservation.dto.ReservationRespDTO;
import Comprehensive_Design_Project.CUK_Compasser.domain.reservation.entity.PickupStatus;
import Comprehensive_Design_Project.CUK_Compasser.domain.reservation.entity.Reservation;
import Comprehensive_Design_Project.CUK_Compasser.domain.reservation.entity.ReservationStatus;
import Comprehensive_Design_Project.CUK_Compasser.domain.reservation.repository.ReservationRepository;
import Comprehensive_Design_Project.CUK_Compasser.domain.store.entity.Store;
import Comprehensive_Design_Project.CUK_Compasser.domain.store.repository.StoreRepository;
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
    private final StoreRepository storeRepository;
    private final RandomBoxRepository randomBoxRepository;
    private final ReservationConverter reservationConverter;

    /**
     * 예약 페이지 목록 조회
     *
     * - 예약 페이지에는 확인 대기중인 예약만 보여준다.
     * - 즉 REQUESTED 상태의 예약만 조회한다.
     *
     * 처리 흐름:
     * 1. 현재 로그인한 점장의 가게를 찾는다.
     * 2. 해당 가게의 REQUESTED 상태 예약만 최신순으로 조회한다.
     * 3. 화면 응답 DTO로 변환해서 반환한다.
     */
    @Override
    @Transactional(readOnly = true)
    public ReservationRespDTO.ReservationListDTO getPendingReservations(Long memberId) {

        Store store = storeRepository.findByStoreManager_MemberId(memberId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.STORE_NOT_FOUND));

        List<Reservation> reservations =
                reservationRepository.findAllByStore_IdAndStatusOrderByCreatedAtDesc(
                        store.getId(), ReservationStatus.REQUESTED
                );

        return reservationConverter.toReservationListDTO(reservations);
    }

    /**
     * 주문 페이지 목록 조회
     *
     * - 주문 페이지에는 처리 완료된 예약만 보여준다.
     * - 즉 APPROVED, REJECTED, CANCELED 상태를 조회한다.
     *
     * 처리 흐름:
     * 1. 현재 로그인한 점장의 가게를 찾는다.
     * 2. 해당 가게의 처리 완료 상태 예약만 최신순으로 조회한다.
     * 3. 화면 응답 DTO로 변환해서 반환한다.
     */
    @Override
    @Transactional(readOnly = true)
    public ReservationRespDTO.ReservationListDTO getProcessedReservations(Long memberId) {
        Store store = storeRepository.findByStoreManager_MemberId(memberId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.STORE_NOT_FOUND));

        List<ReservationStatus> processedStatuses = List.of(
                ReservationStatus.APPROVED,
                ReservationStatus.REJECTED,
                ReservationStatus.CANCELED
        );

        List<Reservation> reservations =
                reservationRepository.findAllByStore_IdAndStatusInOrderByCreatedAtDesc(
                        store.getId(), processedStatuses
                );

        return reservationConverter.toReservationListDTO(reservations);
    }

    /**
     * 예약 수락
     *
     * - REQUESTED 상태의 예약만 APPROVED로 변경할 수 있다.
     * - 승인 시 해당 예약 수량만큼 랜덤박스 재고를 차감한다.
     * - 재고가 부족하면 승인할 수 없다.
     * - 동시 승인 상황에서 재고 꼬임을 막기 위해 RandomBox는 락을 걸고 조회한다.
     *
     * 처리 흐름:
     * 1. 현재 로그인한 점장의 가게를 찾는다.
     * 2. 해당 가게에 속한 예약인지 확인한다.
     * 3. 현재 예약 상태가 REQUESTED인지 검증한다.
     * 4. 연결된 RandomBox를 락(PESSIMISTIC_WRITE)으로 다시 조회한다.
     * 5. 요청 수량만큼 재고를 차감한다.
     * 6. 예약 상태를 APPROVED로 변경한다.
     * 7. 변경된 예약을 DTO로 변환해 반환한다.
     */
    @Override
    @Transactional
    public ReservationRespDTO.ReservationDTO approveReservation(Long reservationId, Long memberId) {
        // 현재 로그인한 점주의 가게 조회
        Store store = storeRepository.findByStoreManager_MemberId(memberId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.STORE_NOT_FOUND));

        // 해당 가게의 예약인지 확인하면서 예약 조회
        Reservation reservation = reservationRepository.findByIdAndStore_Id(reservationId, store.getId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.RESERVATION_NOT_FOUND));

        ReservationStatus currentStatus = reservation.getStatus();

        // 이미 픽업이 끝난 예약은 상태 변경 불가
        if (reservation.getPickupStatus() == PickupStatus.PICKED_UP) {
            throw new GeneralException(ErrorStatus.RESERVATION_ALREADY_PICKED_UP);
        }

        // 사용자가 취소한 예약은 점주가 다시 처리할 수 없음
        if (currentStatus == ReservationStatus.CANCELED) {
            throw new GeneralException(ErrorStatus.INVALID_RESERVATION_STATUS);
        }

        // 이미 승인된 예약은 다시 승인하지 않음
        if (currentStatus == ReservationStatus.APPROVED) {
            throw new GeneralException(ErrorStatus.RESERVATION_ALREADY_APPROVED);
        }

        // 승인 시점에만 실제 재고를 차감해야 하므로 락을 걸고 랜덤박스 재조회
        RandomBox lockedRandomBox = randomBoxRepository.findWithLockById(reservation.getRandomBox().getId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.RANDOM_BOX_NOT_FOUND));

        // 재고 차감
        // 차감 후 0개가 되면 RandomBox 내부에서 자동으로 SOLD_OUT 처리
        lockedRandomBox.decreaseStock(reservation.getRequestedQuantity());

        // 예약 승인 처리
        reservation.approve();

        // 승인 후 상품 준비중 상태로 변경
        reservation.markPreparing();

        return reservationConverter.toReservationDTO(reservation);
    }
    /**
     * 예약 거절
     *
     * - REQUESTED 상태의 예약만 거절할 수 있다.
     * - 거절 사유는 필수다.
     *
     * 처리 흐름:
     * 1. 현재 로그인한 점장의 가게를 찾는다.
     * 2. 해당 가게에 속한 예약인지 확인한다.
     * 3. 현재 예약 상태가 REQUESTED인지 검증한다.
     * 4. 거절 사유가 비어 있지 않은지 확인한다.
     * 5. 예약 상태를 REJECTED로 변경하고 거절 사유를 저장한다.
     * 6. 변경된 예약을 DTO로 변환해 반환한다.
     */
    @Override
    @Transactional
    public ReservationRespDTO.ReservationDTO rejectReservation(Long reservationId, Long memberId,
                                                               ReservationReqDTO request) {
        Store store = storeRepository.findByStoreManager_MemberId(memberId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.STORE_NOT_FOUND));

        Reservation reservation = reservationRepository.findByIdAndStore_Id(reservationId, store.getId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.RESERVATION_NOT_FOUND));

        ReservationStatus currentStatus = reservation.getStatus();

        // 거절 사유 필수
        if (request.getRejectReason() == null || request.getRejectReason().trim().isEmpty()) {
            throw new GeneralException(ErrorStatus.REJECT_REASON_REQUIRED);
        }

        // 이미 픽업 완료된 예약은 변경 불가
        if (reservation.getPickupStatus() == PickupStatus.PICKED_UP) {
            throw new GeneralException(ErrorStatus.RESERVATION_ALREADY_PICKED_UP);
        }

        // 사용자가 이미 취소한 예약은 점주가 건드리지 못하게 막음
        if (currentStatus == ReservationStatus.CANCELED) {
            throw new GeneralException(ErrorStatus.INVALID_RESERVATION_STATUS);
        }

        // 이미 거절 상태면 중복 거절 방지
        if (currentStatus == ReservationStatus.REJECTED) {
            throw new GeneralException(ErrorStatus.RESERVATION_ALREADY_REJECTED);
        }

        // APPROVED -> REJECTED
        // 승인하면서 차감했던 재고를 복구해야 함
        if (currentStatus == ReservationStatus.APPROVED) {
            RandomBox lockedRandomBox = randomBoxRepository.findWithLockById(reservation.getRandomBox().getId())
                    .orElseThrow(() -> new GeneralException(ErrorStatus.RANDOM_BOX_NOT_FOUND));

            lockedRandomBox.increaseStock(reservation.getRequestedQuantity());
        }

        // REQUESTED -> REJECTED 는 재고 변화 없음
        reservation.reject(request.getRejectReason());

        return reservationConverter.toReservationDTO(reservation);
    }
}