package Comprehensive_Design_Project.CUK_Compasser.domain.order.converter;

import Comprehensive_Design_Project.CUK_Compasser.domain.order.entity.OrderStatus;
import Comprehensive_Design_Project.CUK_Compasser.domain.reservation.entity.PaymentStatus;
import Comprehensive_Design_Project.CUK_Compasser.domain.reservation.entity.Reservation;
import Comprehensive_Design_Project.CUK_Compasser.domain.reservation.entity.ReservationStatus;

public class OrderStatusMapper {

    private OrderStatusMapper() {
    }

    /**
     * Reservation의 여러 상태값을 조합해서
     * 사용자 화면에서 보여줄 대표 주문 상태(OrderStatus)를 계산한다.
     *
     * 우선순위:
     * 1. 취소/거절
     * 2. 픽업 완료
     * 3. 픽업 준비 완료
     * 4. 준비중
     * 5. 결제 완료
     * 6. 점주 승인
     * 7. 기본 생성 상태
     */
    /**
     * 사용자 주문 화면에 표시할 대표 상태를 계산하는 클래스
     *
     * 실제 상태 원천은 ReservationStatus, PaymentStatus이며,
     * OrderStatus는 다른 코드와의 호환을 위해 대표 상태로만 사용한다.
     */
    public static OrderStatus resolve(Reservation reservation) {
        if (reservation == null) {
            return OrderStatus.CREATED;
        }

        ReservationStatus reservationStatus = reservation.getStatus();
        PaymentStatus paymentStatus = reservation.getPaymentStatus();

        // 사용자가 취소했거나 점주가 거절한 경우
        if (reservationStatus == ReservationStatus.CANCELED ||
                reservationStatus == ReservationStatus.REJECTED) {
            return OrderStatus.CANCELED;
        }

        // 결제 완료
        if (paymentStatus == PaymentStatus.PAID) {
            return OrderStatus.PAID;
        }

        // 승인만 된 상태는 아직 사용자 화면에서는 CREATED 또는 PAID 이전 단계로 볼 수 있음
        // 정책상 별도 APPROVED가 없으므로 CREATED로 흡수
        return OrderStatus.CREATED;
    }
}