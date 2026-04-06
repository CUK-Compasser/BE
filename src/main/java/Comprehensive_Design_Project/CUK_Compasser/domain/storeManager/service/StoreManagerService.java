package Comprehensive_Design_Project.CUK_Compasser.domain.storeManager.service;


import Comprehensive_Design_Project.CUK_Compasser.domain.member.dto.MemberRespDTO;
import Comprehensive_Design_Project.CUK_Compasser.domain.member.entity.Member;
import Comprehensive_Design_Project.CUK_Compasser.domain.member.repository.MemberRepository;
import Comprehensive_Design_Project.CUK_Compasser.domain.reservation.entity.Reservation;
import Comprehensive_Design_Project.CUK_Compasser.domain.reservation.entity.ReservationStatus;
import Comprehensive_Design_Project.CUK_Compasser.domain.reservation.repository.ReservationRepository;
import Comprehensive_Design_Project.CUK_Compasser.domain.reward.entity.Reward;
import Comprehensive_Design_Project.CUK_Compasser.domain.reward.repository.RewardRepository;
import Comprehensive_Design_Project.CUK_Compasser.domain.rewardHistory.entity.RewardHistory;
import Comprehensive_Design_Project.CUK_Compasser.domain.rewardHistory.repository.RewardHistoryRepository;
import Comprehensive_Design_Project.CUK_Compasser.domain.store.entity.Store;
import Comprehensive_Design_Project.CUK_Compasser.domain.store.repository.StoreRepository;
import Comprehensive_Design_Project.CUK_Compasser.domain.storeManager.converter.StoreManagerConverter;
import Comprehensive_Design_Project.CUK_Compasser.domain.storeManager.dto.req.StoreManagerReqDTO;
import Comprehensive_Design_Project.CUK_Compasser.domain.storeManager.dto.resp.StoreManagerRespDTO;
import Comprehensive_Design_Project.CUK_Compasser.domain.storeManager.repository.StoreManagerRepository;
import Comprehensive_Design_Project.CUK_Compasser.global.common.apiPayload.code.status.ErrorStatus;
import Comprehensive_Design_Project.CUK_Compasser.global.common.apiPayload.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StoreManagerService {

    private final MemberRepository memberRepository;
    private final StoreRepository storeRepository;
    private final RewardRepository rewardRepository;
    private final RewardHistoryRepository rewardHistoryRepository;
    private final StoreManagerRepository storeManagerRepository;
    private final ReservationRepository reservationRepository;
    private final RedisTemplate redisTemplate;

    @Transactional(readOnly = true)
    public StoreManagerRespDTO.GetMemberRewardDTO checkingQR(Long storeManagerId, MemberRespDTO.QRDTO qrDTO) {

        /*String token = (String) redisTemplate.opsForValue().get("qr:" + qrDTO.getToken());
        if (token == null) {
            throw new GeneralException(ErrorStatus.QR_EXPIRED);
        }*/

        Optional<StoreManagerRespDTO.GetMemberRewardDTO> memberReward = storeManagerRepository.getMemberRewardByMemberIdAndStoreId(
                qrDTO.getMemberId(),
                storeManagerId
                , ReservationStatus.APPROVED);
        if (memberReward.isPresent()) {
            return memberReward.get();
        } else {
            throw new GeneralException(ErrorStatus.RESERVATION_NOT_FOUND);
        }
    }

    @Transactional
    public void writingReward(StoreManagerReqDTO.WritingRewardDTO dto) {
        Reward reward = rewardRepository.findById(dto.getRewardId()).orElseThrow(() -> new GeneralException(ErrorStatus.REWARD_NOT_FOUND));

        Member member = memberRepository.findById(dto.getMemberId()).orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));
        Store store = storeRepository.findById(dto.getStoreId()).orElseThrow(() -> new GeneralException(ErrorStatus.STORE_NOT_FOUND));
        // 있으면 Increase
        if (reward != null) {
            reward.increasePoint();

        } else {
            rewardRepository.save(Reward.createNewReward(member, store));
        }

        rewardHistoryRepository.save(RewardHistory.earnReward(member, store));
    }
}
