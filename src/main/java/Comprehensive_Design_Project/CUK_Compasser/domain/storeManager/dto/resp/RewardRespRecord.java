package Comprehensive_Design_Project.CUK_Compasser.domain.storeManager.dto.resp;

import java.time.LocalDateTime;

public record RewardRespRecord(
        Long memberId,
        String nickname,
        String email,
        Long storeId,
        String boxName,
        Integer totalPrice,
        Long rewardId,
        Integer stamp,
        Integer coupon,
        LocalDateTime createdAt
) {
}
/*
 * 반환해야 하는 정보
 * member: nickname, email // memberId
 * store: store_id // store_manager.getStore().getId()
 * order: totalPrice // findByMemberIdAndStoreId, randomBox와 연관 설정 예정.
 * randomBox: boxName // order.getRandomBox().getName()
 * reward: stamp, coupon // findByMemberIdAndStoreId
 * ----
 * 주어지는 정보: memberId, storeManagerId
 * findRewardRespRecordByMemberIdAndStoreManagerId
 *
 * */