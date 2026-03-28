package Comprehensive_Design_Project.CUK_Compasser.domain.reservation.dto;

import lombok.*;

public class KakaoPayRespDTO {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ReadyResponseDTO {
        private String tid;
        private String next_redirect_app_url;
        private String next_redirect_mobile_url;
        private String next_redirect_pc_url;
        private String created_at;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ApproveResponseDTO {
        private String aid;
        private String tid;
        private String cid;
        private String partner_order_id;
        private String partner_user_id;
        private String item_name;
        private Integer quantity;
        private Amount amount;
        private String approved_at;

        @Getter
        @NoArgsConstructor
        @AllArgsConstructor
        @Builder
        public static class Amount {
            private Integer total;
            private Integer tax_free;
            private Integer vat;
            private Integer discount;
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReadyResultDTO {
        private Long reservationId;
        private String tid;
        private String redirectUrl;
        private String paymentStatus;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ApproveResultDTO {
        private Long reservationId;
        private String paymentMethod;
        private String paymentStatus;
        private String approvedAt;
    }
}