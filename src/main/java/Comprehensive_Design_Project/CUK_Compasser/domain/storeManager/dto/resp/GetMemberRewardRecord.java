package Comprehensive_Design_Project.CUK_Compasser.domain.storeManager.dto.resp;

public record GetMemberRewardRecord(

         Long rewardId,
         Long storeId,
         Long memberId,
         String nickname,
         String email,
         String randomBoxName,
         Integer totalPrice,
         Integer stamp,
         Integer coupon
) {
}
