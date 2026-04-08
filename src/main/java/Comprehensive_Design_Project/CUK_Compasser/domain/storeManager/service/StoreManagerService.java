package Comprehensive_Design_Project.CUK_Compasser.domain.storeManager.service;


import Comprehensive_Design_Project.CUK_Compasser.domain.member.entity.Member;
import Comprehensive_Design_Project.CUK_Compasser.domain.member.repository.MemberRepository;
import Comprehensive_Design_Project.CUK_Compasser.domain.reservation.entity.ReservationStatus;
import Comprehensive_Design_Project.CUK_Compasser.domain.reservation.repository.ReservationRepository;
import Comprehensive_Design_Project.CUK_Compasser.domain.reward.entity.Reward;
import Comprehensive_Design_Project.CUK_Compasser.domain.reward.repository.RewardRepository;
import Comprehensive_Design_Project.CUK_Compasser.domain.rewardHistory.entity.RewardHistory;
import Comprehensive_Design_Project.CUK_Compasser.domain.rewardHistory.repository.RewardHistoryRepository;
import Comprehensive_Design_Project.CUK_Compasser.domain.store.entity.Store;
import Comprehensive_Design_Project.CUK_Compasser.domain.store.repository.StoreRepository;
import Comprehensive_Design_Project.CUK_Compasser.domain.storeManager.dto.req.StoreManagerReqDTO;
import Comprehensive_Design_Project.CUK_Compasser.domain.storeManager.dto.resp.GetMemberRewardRecord;
import Comprehensive_Design_Project.CUK_Compasser.domain.storeManager.repository.StoreManagerRepository;
import Comprehensive_Design_Project.CUK_Compasser.global.common.apiPayload.code.status.ErrorStatus;
import Comprehensive_Design_Project.CUK_Compasser.global.common.apiPayload.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class StoreManagerService {

    private final MemberRepository memberRepository;
    private final StoreRepository storeRepository;
    private final RewardRepository rewardRepository;
    private final RewardHistoryRepository rewardHistoryRepository;
    private final StoreManagerRepository storeManagerRepository;
    private final RedisTemplate redisTemplate;

    @Transactional(readOnly = true)
    public GetMemberRewardRecord checkingQR(Long storeManagerId, String token, Long memberId) {

        String memberToken = (String) redisTemplate.opsForValue().get("qr:" + memberId);
        if (memberToken == null) {
            throw new GeneralException(ErrorStatus.QR_EXPIRED);
        }
        else if (!memberToken.equals(token)) {
            log.info("memberToken: {}, qrToken: {}", memberToken, token);
            throw new GeneralException(ErrorStatus.WRONG_QR);
        }

        if (!memberRepository.existsById(memberId)){
            throw new GeneralException(ErrorStatus.USER_NOT_FOUND);
        }

        return storeManagerRepository.getMemberRewardByMemberIdAndStoreId(
                memberId,
                storeManagerId,
                ReservationStatus.APPROVED)
                .orElseThrow(() -> new GeneralException(ErrorStatus.RESERVATION_NOT_FOUND));
    }

    /**
     * 앞 API 에서 이미 올바른 값을 주었음을 가정하여 getReference를 사용
     * 신뢰!
     * @return void
     * */
    @Transactional
    public void writingReward(StoreManagerReqDTO.WritingRewardDTO dto) {
        Reward reward = rewardRepository.findById(dto.getRewardId())
                .orElse(null);

        Member memberReference = memberRepository.getReferenceById(dto.getMemberId());
        Store storeReference = storeRepository.getReferenceById(dto.getStoreId());

        /*Member member = memberRepository.findById(dto.getMemberId()).orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));
        Store store = storeRepository.findById(dto.getStoreId()).orElseThrow(() -> new GeneralException(ErrorStatus.STORE_NOT_FOUND));*/
        // 있으면 Increase
        if (reward != null) {
            reward.increasePoint();

        } else {
            rewardRepository.save(Reward.createNewReward(memberReference, storeReference));
        }
        rewardHistoryRepository.save(RewardHistory.earnReward(memberReference, storeReference));
    }

}
