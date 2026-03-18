package Comprehensive_Design_Project.CUK_Compasser.domain.reward.dto;

public record SummaryMemberReward(
        Long stamp,
        Long useCouponCnt,
        Long unboxingCnt
) {
    public SummaryMemberReward {
        stamp = (stamp == null) ? 0 : stamp;
        useCouponCnt = (useCouponCnt == null) ? 0 : useCouponCnt;
        unboxingCnt = (unboxingCnt == null) ? 0 : unboxingCnt;
    }
    public SummaryMemberReward(Long stamp, Long useCouponCnt) {
        // 3번째 인자인 unboxedCount에 기본값 0을 주입하며 위 생성자를 호출함
        this(stamp, useCouponCnt, 0L);
    }
    // record = private final = setter 불가능
    public SummaryMemberReward withUnboxedCount(Long unboxingCnt) {
        return new SummaryMemberReward(this.stamp, this.useCouponCnt, unboxingCnt);
    }
}
