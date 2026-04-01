package Comprehensive_Design_Project.CUK_Compasser.domain.reservation.dto;

import lombok.*;

public class KakaoPayReqDTO {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReadyRequestDTO {
        private String cid;
        private String partner_order_id;
        private String partner_user_id;
        private String item_name;
        private Integer quantity;
        private Integer total_amount;
        private Integer tax_free_amount;
        private String approval_url;
        private String cancel_url;
        private String fail_url;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ApproveRequestDTO {
        private String cid;
        private String tid;
        private String partner_order_id;
        private String partner_user_id;
        private String pg_token;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CancelReqDTO {
        private String cid;
        private String tid;
        private Integer cancel_amount;
        private Integer cancel_tax_free_amount;
    }
}