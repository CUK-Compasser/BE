package Comprehensive_Design_Project.CUK_Compasser.domain.storeManager.dto.resp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class StoreManagerRespDTO {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetMemberRewardDTO {
        private Long rewardId;
        private Long storeId;
        private Long memberId;
        private String nickname;
        private String email;
        private String randomBoxName;
        private Integer totalPrice;
        private Integer stamp;
        private Integer coupon;
    }
}
