package Comprehensive_Design_Project.CUK_Compasser.domain.reward.dto;

public record RewardEachStoreRecord(
        String storeName,
        long stamp,
        long coupon,
        long useCouponCnt
) {
}
