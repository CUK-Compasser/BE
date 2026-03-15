package Comprehensive_Design_Project.CUK_Compasser.domain.storeManager.dto.resp;

import java.time.LocalDateTime;

public record RewardRespRecord(
        Long memberId,
        String nickname,
        Long storeId,
        Long rewardId,
        Integer stamp,
        Integer coupon,
        LocalDateTime createdAt
) {
}
