package Comprehensive_Design_Project.CUK_Compasser.domain.order.service;

import Comprehensive_Design_Project.CUK_Compasser.domain.member.entity.Member;
import Comprehensive_Design_Project.CUK_Compasser.domain.member.repository.MemberRepository;
import Comprehensive_Design_Project.CUK_Compasser.domain.order.converter.OrderConverter;
import Comprehensive_Design_Project.CUK_Compasser.domain.order.dto.OrderReqDTO;
import Comprehensive_Design_Project.CUK_Compasser.domain.order.dto.OrderRespDTO;
import Comprehensive_Design_Project.CUK_Compasser.domain.randomBox.entity.RandomBox;
import Comprehensive_Design_Project.CUK_Compasser.domain.randomBox.entity.SaleStatus;
import Comprehensive_Design_Project.CUK_Compasser.domain.randomBox.repository.RandomBoxRepository;
import Comprehensive_Design_Project.CUK_Compasser.domain.reservation.dto.KakaoPayReqDTO;
import Comprehensive_Design_Project.CUK_Compasser.domain.reservation.dto.KakaoPayRespDTO;
import Comprehensive_Design_Project.CUK_Compasser.domain.reservation.entity.PaymentStatus;
import Comprehensive_Design_Project.CUK_Compasser.domain.reservation.entity.Reservation;
import Comprehensive_Design_Project.CUK_Compasser.domain.reservation.entity.ReservationStatus;
import Comprehensive_Design_Project.CUK_Compasser.domain.reservation.repository.ReservationRepository;
import Comprehensive_Design_Project.CUK_Compasser.domain.reservation.service.KakaoPayClient;
import Comprehensive_Design_Project.CUK_Compasser.global.common.apiPayload.code.status.ErrorStatus;
import Comprehensive_Design_Project.CUK_Compasser.global.common.apiPayload.exception.GeneralException;
import Comprehensive_Design_Project.CUK_Compasser.global.config.KakaoPayProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderServiceImpl implements OrderService {

    private final MemberRepository memberRepository;
    private final RandomBoxRepository randomBoxRepository;
    private final ReservationRepository reservationRepository;
    private final KakaoPayClient kakaoPayClient;
    private final KakaoPayProperties kakaoPayProperties;

    /**
     * 사용자 주문 생성
     * - 사용자 주문 화면에서 사용할 Reservation 데이터를 생성한다.
     * - 주문 생성 시 예약/결제/픽업 상태의 기본값을 함께 세팅한다.
     * - 결제는 이후 카카오페이 ready/approve API에서 진행한다.
     */
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
                .paymentStatus(PaymentStatus.PENDING)
                .build();

        Reservation savedReservation = reservationRepository.save(reservation);

        return OrderConverter.toCreateOrderResultDTO(savedReservation);
    }

    /**
     * 사용자 주문 취소
     * - 아직 점주가 처리하지 않은 REQUESTED 상태의 주문만 취소할 수 있다.
     * - 결제 전 주문은 paymentStatus 를 CANCELED 로 변경한다.
     * - 이미 결제 완료된 주문은 카카오 결제 취소(환불) 후 paymentStatus 를 REFUNDED 로 변경한다.
     */
    @Override
    @Transactional
    public OrderRespDTO.CancelOrderResultDTO cancelOrder(Long memberId, Long orderId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        Reservation reservation = reservationRepository.findByIdAndMember(orderId, member)
                .orElseThrow(() -> new GeneralException(ErrorStatus.RESERVATION_NOT_FOUND));

        validateCancelOrder(reservation);

        if (reservation.getPaymentStatus() == PaymentStatus.PAID) {
            cancelKakaoPayPayment(reservation);
            reservation.markRefunded();
        } else {
            reservation.setPaymentStatus(PaymentStatus.CANCELED);
        }

        reservation.setStatus(ReservationStatus.CANCELED);

        return OrderConverter.toCancelOrderResultDTO(
                reservation,
                "주문이 취소되었습니다."
        );
    }

    /**
     * 사용자 주문 상태 조회
     * - 사용자 화면에서 보여줄 주문 상태 상세 정보를 반환한다.
     */
    @Override
    public OrderRespDTO.OrderStatusDTO getOrderStatus(Long memberId, Long orderId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        Reservation reservation = reservationRepository.findByIdAndMember(orderId, member)
                .orElseThrow(() -> new GeneralException(ErrorStatus.RESERVATION_NOT_FOUND));

        return OrderConverter.toOrderStatusDTO(reservation);
    }

    /**
     * 주문 생성 가능 여부 검증
     */
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

    /**
     * 주문 취소 가능 여부 검증
     * - 아직 점주가 처리하지 않은 REQUESTED 상태만 취소 가능하다.
     * - 이미 취소되었거나 환불 완료된 주문은 다시 취소할 수 없다.
     */
    private void validateCancelOrder(Reservation reservation) {
        if (reservation.getStatus() != ReservationStatus.REQUESTED) {
            throw new GeneralException(ErrorStatus.ORDER_CANCEL_NOT_ALLOWED);
        }

        if (reservation.getPaymentStatus() == PaymentStatus.REFUNDED ||
                reservation.getPaymentStatus() == PaymentStatus.CANCELED) {
            throw new GeneralException(ErrorStatus.ORDER_CANCEL_NOT_ALLOWED);
        }
    }

    /**
     * 카카오페이 결제 취소(환불)
     * - 이미 결제 완료된 reservation 에 대해서만 호출한다.
     * - 카카오 취소 응답의 tid 가 기존 결제 tid 와 일치하는지 검증한다.
     */
    private void cancelKakaoPayPayment(Reservation reservation) {
        if (!StringUtils.hasText(reservation.getPaymentTid())) {
            throw new GeneralException(ErrorStatus.PAYMENT_TID_NOT_FOUND);
        }

        KakaoPayReqDTO.CancelReqDTO request = KakaoPayReqDTO.CancelReqDTO.builder()
                .cid(kakaoPayProperties.getCid())
                .tid(reservation.getPaymentTid())
                .cancel_amount(reservation.getTotalPrice())
                .cancel_tax_free_amount(0)
                .build();

        KakaoPayRespDTO.CancelRespDTO response = kakaoPayClient.cancel(request);

        if (response == null || !reservation.getPaymentTid().equals(response.getTid())) {
            throw new GeneralException(ErrorStatus.INVALID_PAYMENT_INFO);
        }
    }
}