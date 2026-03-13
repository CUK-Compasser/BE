package Comprehensive_Design_Project.CUK_Compasser.domain.rewardHistory.entity;


import Comprehensive_Design_Project.CUK_Compasser.domain.member.entity.Member;
import Comprehensive_Design_Project.CUK_Compasser.domain.store.entity.Store;
import Comprehensive_Design_Project.CUK_Compasser.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class RewardHistory extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RewardType type; // EARN or USE

    public static RewardHistory earnReward(Member member, Store store){
        return RewardHistory.builder().member(member).store(store).type(RewardType.EARN).build();
    }
    public static RewardHistory useReward(Member member, Store store){
        return RewardHistory.builder().member(member).store(store).type(RewardType.COUPON).build();
    }

}
