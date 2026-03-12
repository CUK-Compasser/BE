package Comprehensive_Design_Project.CUK_Compasser.domain.reservation.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

public class ReservationReqDTO {

    @Getter
    @NoArgsConstructor
    public static class RejectDTO {
        private String rejectReason;
    }
}