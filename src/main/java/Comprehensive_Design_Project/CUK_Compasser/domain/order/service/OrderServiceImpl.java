package Comprehensive_Design_Project.CUK_Compasser.domain.order.service;

import Comprehensive_Design_Project.CUK_Compasser.domain.member.entity.Member;
import Comprehensive_Design_Project.CUK_Compasser.domain.member.repository.MemberRepository;
import Comprehensive_Design_Project.CUK_Compasser.domain.order.converter.OrderConverter;
import Comprehensive_Design_Project.CUK_Compasser.domain.order.dto.OrderReqDTO;
import Comprehensive_Design_Project.CUK_Compasser.domain.order.dto.OrderRespDTO;
import Comprehensive_Design_Project.CUK_Compasser.domain.randomBox.entity.RandomBox;
import Comprehensive_Design_Project.CUK_Compasser.domain.randomBox.entity.SaleStatus;
import Comprehensive_Design_Project.CUK_Compasser.domain.randomBox.repository.RandomBoxRepository;
import Comprehensive_Design_Project.CUK_Compasser.domain.reservation.entity.Reservation;
import Comprehensive_Design_Project.CUK_Compasser.domain.reservation.entity.ReservationStatus;
import Comprehensive_Design_Project.CUK_Compasser.domain.reservation.repository.ReservationRepository;
import Comprehensive_Design_Project.CUK_Compasser.global.common.apiPayload.code.status.ErrorStatus;
import Comprehensive_Design_Project.CUK_Compasser.global.common.apiPayload.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderServiceImpl implements OrderService {

    private final MemberRepository memberRepository;
    private final RandomBoxRepository randomBoxRepository;
    private final ReservationRepository reservationRepository;

    @Override
    @Transactional
    public OrderRespDTO.CreateOrderResultDTO createOrder(Long memberId, OrderReqDTO.CreateOrderDTO request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        RandomBox randomBox = randomBoxRepository.findById(request.getRandomBoxId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.RANDOM_BOX_NOT_FOUND));

        validateCreateOrder(randomBox, request.getQuantity());

        int totalPrice = randomBox.getPrice() * request.getQuantity();

        Reservation reservation = Reservation.builder()
                .member(member)
                .store(randomBox.getStore())
                .randomBox(randomBox)
                .requestedQuantity(request.getQuantity())
                .totalPrice(totalPrice)
                .status(ReservationStatus.REQUESTED)
                .build();

        Reservation savedReservation = reservationRepository.save(reservation);

        return OrderConverter.toCreateOrderResultDTO(savedReservation);
    }

    @Override
    @Transactional
    public OrderRespDTO.CompleteOrderResultDTO completeOrder(Long memberId, Long reservationId, OrderReqDTO.CompleteOrderDTO request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        Reservation reservation = reservationRepository.findByIdAndMember(reservationId, member)
                .orElseThrow(() -> new GeneralException(ErrorStatus.RESERVATION_NOT_FOUND));

        if (reservation.getStatus() == ReservationStatus.REJECTED ||
                reservation.getStatus() == ReservationStatus.CANCELED) {
            throw new GeneralException(ErrorStatus.RESERVATION_ALREADY_PROCESSED);
        }

        member.setBankType(request.getBankType());

        return OrderConverter.toCompleteOrderResultDTO(
                reservation,
                "선택한 은행 정보가 저장되었습니다. 실제 입금 확인은 사장님이 처리합니다."
        );
    }

    @Override
    public OrderRespDTO.OrderStatusDTO getOrderStatus(Long memberId, Long reservationId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        Reservation reservation = reservationRepository.findByIdAndMember(reservationId, member)
                .orElseThrow(() -> new GeneralException(ErrorStatus.RESERVATION_NOT_FOUND));

        return OrderConverter.toOrderStatusDTO(reservation);
    }

    private void validateCreateOrder(RandomBox randomBox, Integer quantity) {
        if (quantity == null || quantity < 1) {
            throw new GeneralException(ErrorStatus.INVALID_ORDER_QUANTITY);
        }

        if (randomBox.getSaleStatus() != SaleStatus.READY) {
            throw new GeneralException(ErrorStatus.RANDOM_BOX_NOT_ON_SALE);
        }

        if (randomBox.getStock() < quantity) {
            throw new GeneralException(ErrorStatus.RANDOM_BOX_STOCK_NOT_ENOUGH);
        }

        if (randomBox.getBuyLimit() != null && quantity > randomBox.getBuyLimit()) {
            throw new GeneralException(ErrorStatus.RANDOM_BOX_BUY_LIMIT_EXCEEDED);
        }
    }
}