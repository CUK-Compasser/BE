package Comprehensive_Design_Project.CUK_Compasser.domain.order.dto;

import Comprehensive_Design_Project.CUK_Compasser.domain.member.entity.BankType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

public class OrderReqDTO {

    @Getter
    public static class CreateOrderDTO {

        @NotNull
        private Long randomBoxId;

        @NotNull
        @Min(1)
        private Integer quantity;
    }

    @Getter
    public static class CompleteOrderDTO {

        @NotNull
        private BankType bankType;
    }
}