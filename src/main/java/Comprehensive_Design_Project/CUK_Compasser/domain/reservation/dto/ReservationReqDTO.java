package Comprehensive_Design_Project.CUK_Compasser.domain.reservation.dto;

import Comprehensive_Design_Project.CUK_Compasser.domain.reservation.entity.ReservationStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReservationReqDTO {
    private ReservationStatus status;
    private String rejectReason;
}