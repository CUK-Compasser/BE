package Comprehensive_Design_Project.CUK_Compasser.domain.reward.converter;

import Comprehensive_Design_Project.CUK_Compasser.domain.member.dto.MemberRespDTO;
import Comprehensive_Design_Project.CUK_Compasser.domain.reward.entity.Reward;

import java.util.List;

public class RewardConverter {


    public static List<MemberRespDTO.RewardListDTO> toRewardListDTO(List<Reward> rewardList) {
        return rewardList.stream()
                .map(reward ->
                        MemberRespDTO.RewardListDTO.builder()
                                .rewardId(reward.getId())
                                .points(reward.getStamp())
                                .storeName(reward.getStore().getStoreName())
                                .build()
                )
                .toList();
    }
}
