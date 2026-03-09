package Comprehensive_Design_Project.CUK_Compasser.domain.storeManager.service;


import Comprehensive_Design_Project.CUK_Compasser.domain.member.dto.MemberRespDTO;
import Comprehensive_Design_Project.CUK_Compasser.domain.member.entity.Member;
import Comprehensive_Design_Project.CUK_Compasser.domain.member.repository.MemberRepository;
import Comprehensive_Design_Project.CUK_Compasser.domain.reward.entity.Reward;
import Comprehensive_Design_Project.CUK_Compasser.domain.reward.repository.RewardRepository;
import Comprehensive_Design_Project.CUK_Compasser.domain.store.entity.Store;
import Comprehensive_Design_Project.CUK_Compasser.domain.store.repository.StoreRepository;
import Comprehensive_Design_Project.CUK_Compasser.domain.storeManager.dto.StoreManagerRespDTO;
import Comprehensive_Design_Project.CUK_Compasser.domain.storeManager.entity.StoreManager;
import Comprehensive_Design_Project.CUK_Compasser.domain.storeManager.repository.StoreManagerRepository;
import Comprehensive_Design_Project.CUK_Compasser.global.common.apiPayload.code.status.ErrorStatus;
import Comprehensive_Design_Project.CUK_Compasser.global.common.apiPayload.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StoreManagerService {

    private final MemberRepository memberRepository;
    private final StoreRepository storeRepository;
    private final RewardRepository rewardRepository;
    private final RedisTemplate redisTemplate;

    @Transactional(readOnly = true)
    public StoreManagerRespDTO.GetMemberRewardDTO checkingQR (Long storeManagerId, MemberRespDTO.QRDTO qrDTO) {

        String token = (String) redisTemplate.opsForValue().get("qr:" + qrDTO.getToken());
        if (token == null) {
            throw new GeneralException(ErrorStatus.QR_EXPIRED);
        }

        // storeManagerId -> storeRepository 확인
        Store store = storeRepository.findByStoreManager_Id(storeManagerId).orElseThrow(() -> new GeneralException(ErrorStatus.STORE_NOT_FOUND));

        // Member member = memberRepository.findById(qrDTO.getMemberId()).orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        // 특정 가게에 특정 사용자에 대한 정보 조회 -> rewardRepository 조회 필요
        Reward reward = rewardRepository.findByMember_IdAndStore_Id(qrDTO.getMemberId(), store.getId()).orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        // null 인 경우 첫 적립 DTO 반환
        if (reward == null) {
            return StoreManagerRespDTO.GetMemberRewardDTO.builder().storeId(store.getId()).memberId(qrDTO.getMemberId()).points(null).createdAt(null).build();
        }
        // null이 아닌 경우 해당 DTO 반환
        return StoreManagerRespDTO.GetMemberRewardDTO.builder().storeId(store.getId()).memberId(qrDTO.getMemberId()).points(reward.getPoints()).createdAt(reward.getCreatedAt()).build();
    }

    @Transactional
    public void writingReward (Long storeManagerId){

        // findByStoreManagerID + Eager Fetch "Store" -> get store_id

        // rewardRepository 조회

        // 있으면 Increase

        // 없으면 new & save

    }
}
