package Comprehensive_Design_Project.CUK_Compasser.domain.storeManager.service;


import Comprehensive_Design_Project.CUK_Compasser.domain.member.dto.MemberRespDTO;
import Comprehensive_Design_Project.CUK_Compasser.domain.member.entity.Member;
import Comprehensive_Design_Project.CUK_Compasser.domain.member.repository.MemberRepository;
import Comprehensive_Design_Project.CUK_Compasser.domain.reward.entity.Reward;
import Comprehensive_Design_Project.CUK_Compasser.domain.reward.repository.RewardRepository;
import Comprehensive_Design_Project.CUK_Compasser.domain.rewardHistory.entity.RewardHistory;
import Comprehensive_Design_Project.CUK_Compasser.domain.rewardHistory.repository.RewardHistoryRepository;
import Comprehensive_Design_Project.CUK_Compasser.domain.store.entity.Store;
import Comprehensive_Design_Project.CUK_Compasser.domain.store.repository.StoreRepository;
import Comprehensive_Design_Project.CUK_Compasser.domain.storeManager.dto.req.StoreManagerReqDTO;
import Comprehensive_Design_Project.CUK_Compasser.domain.storeManager.dto.resp.RewardRespRecord;
import Comprehensive_Design_Project.CUK_Compasser.domain.storeManager.dto.resp.StoreManagerRespDTO;
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
    private final RewardHistoryRepository  rewardHistoryRepository;
    private final RedisTemplate redisTemplate;

    @Transactional(readOnly = true)
    public StoreManagerRespDTO.GetMemberRewardDTO checkingQR (Long storeManagerId, MemberRespDTO.QRDTO qrDTO) {

        String token = (String) redisTemplate.opsForValue().get("qr:" + qrDTO.getToken());
        if (token == null) {
            throw new GeneralException(ErrorStatus.QR_EXPIRED);
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

        RewardRespRecord summary = rewardRepository.findRewardRecord(qrDTO.getMemberId(), storeManagerId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.STORE_NOT_FOUND)); // 매장이나 유저가 없을 때

        // 3. 응답 DTO 조립
        return StoreManagerRespDTO.GetMemberRewardDTO.builder()
                .rewardId(summary.rewardId()) // 첫 적립이면 null
                .storeId(summary.storeId())
                .memberId(summary.memberId())
                .nickname(summary.nickname())
                .stamp(summary.rewardId() == null ? 0 : summary.stamp())
                .coupon(summary.rewardId() == null ? 0 : summary.coupon())
                .createdAt(summary.createdAt())
                .build();

        /*// storeManagerId -> storeRepository 확인
        Store store = storeRepository.findByStoreManager_Id(storeManagerId).orElseThrow(() -> new GeneralException(ErrorStatus.STORE_NOT_FOUND));

        Member member = memberRepository.findById(qrDTO.getMemberId()).orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        // 특정 가게에 특정 사용자에 대한 정보 조회 -> rewardRepository 조회 필요
        Reward reward = rewardRepository.findByMember_IdAndStore_Id(qrDTO.getMemberId(), store.getId()).orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        // null 인 경우 첫 적립 DTO 반환
        if (reward == null) {
            return StoreManagerRespDTO.GetMemberRewardDTO.builder()
                    .rewardId(null)
                    .storeId(store.getId())
                    .memberId(member.getId())
                    .nickname(member.getNickname())
                    .stamp(0)
                    .coupon(0)
                    .createdAt(null)
                    .build();
        }
        // null이 아닌 경우 해당 DTO 반환
        return StoreManagerRespDTO.GetMemberRewardDTO.builder()
                .rewardId(reward.getId())
                .storeId(store.getId())
                .memberId(member.getId())
                .nickname(member.getNickname())
                .stamp(reward.getStamp())
                .coupon(reward.getCoupon())
                .createdAt(reward.getCreatedAt())
                .build();*/
    }

    @Transactional
    public void writingReward(StoreManagerReqDTO.WritingRewardDTO dto) {
        Reward reward = rewardRepository.findById(dto.getRewardId()).orElseThrow(() -> new GeneralException(ErrorStatus.REWARD_NOT_FOUND));

        Member member = memberRepository.findById(dto.getMemberId()).orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));
        Store store = storeRepository.findById(dto.getStoreId()).orElseThrow(() -> new GeneralException(ErrorStatus.STORE_NOT_FOUND));
        // 있으면 Increase
        if (reward != null) {
            reward.increasePoint();

        }
        else{
            rewardRepository.save(Reward.createNewReward(member, store));
        }

        rewardHistoryRepository.save(RewardHistory.earnReward(member, store));
    }
}
