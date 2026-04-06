package Comprehensive_Design_Project.CUK_Compasser.domain.storeManager.converter;

import Comprehensive_Design_Project.CUK_Compasser.domain.member.entity.Member;
import Comprehensive_Design_Project.CUK_Compasser.domain.reservation.entity.Reservation;
import Comprehensive_Design_Project.CUK_Compasser.domain.reward.entity.Reward;
import Comprehensive_Design_Project.CUK_Compasser.domain.store.entity.Store;
import Comprehensive_Design_Project.CUK_Compasser.domain.storeManager.dto.resp.StoreManagerRespDTO;

public class StoreManagerConverter {

    public static StoreManagerRespDTO.GetMemberRewardDTO toGetMemberRewardDTO(Member member, Store store, Reservation reservation, Reward reward) {

        return StoreManagerRespDTO.GetMemberRewardDTO.builder()
                .rewardId(reward == null ? 0L : reward.getId())
                .storeId(store.getId())
                .memberId(member.getId())
                .email(member.getEmail())
                .nickname(member.getNickname())
                .stamp(reward == null ? 0 : reward.getStamp())
                .coupon(reward == null ? 0 : reward.getCoupon())
                .randomBoxName(reservation.getRandomBox().getBoxName())
                .totalPrice(reservation.getTotalPrice())
                .build();
    }
}
