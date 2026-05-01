package Comprehensive_Design_Project.CUK_Compasser.domain.reward.entity;

import Comprehensive_Design_Project.CUK_Compasser.domain.member.entity.Member;
import Comprehensive_Design_Project.CUK_Compasser.domain.store.entity.Store;
import Comprehensive_Design_Project.CUK_Compasser.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "rewards",
        indexes = {
                @Index(name = "idx_rewards_member", columnList = "member_id"),
                @Index(name = "idx_rewards_store", columnList = "store_id")
        }
)
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Reward extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id", nullable = false, foreignKey = @ForeignKey(name = "fk_rewards_member"))
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "store_id", nullable = false, foreignKey = @ForeignKey(name = "fk_rewards_store"))
    private Store store;

    @Column(nullable = false)
    private Integer stamp; // 적립 도장

    @Column(nullable = false)
    private Integer coupon; // 적립 쿠폰

    @Column(nullable = false)
    private Integer useCouponCnt; // 쿠폰 사용 횟수

    @Column(length = 100)
    private String reason;

    /*@PrePersist
    void prePersist() {
        this.createdAt = LocalDateTime.now();
    }*/

    public static Reward createNewReward (Member member, Store store){
        return Reward.builder()
                .member(member)
                .store(store)
                .stamp(1) // 기본 적립 후 스탬프 생성이므로
                .coupon(0)
                .reason("")
                .build();
    }

    public void increasePoint (){
        this.stamp++;
    }

}