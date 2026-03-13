package Comprehensive_Design_Project.CUK_Compasser.domain.storeManager.dto.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class StoreManagerReqDTO {

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class WritingRewardDTO {
        private Long rewardId;
        private Long storeId;
        private Long memberId;
    }

}
