package Comprehensive_Design_Project.CUK_Compasser.domain.member.service;

import Comprehensive_Design_Project.CUK_Compasser.domain.member.dto.MemberRespDTO;
import Comprehensive_Design_Project.CUK_Compasser.domain.member.entity.Member;
import Comprehensive_Design_Project.CUK_Compasser.domain.member.repository.MemberRepository;
import Comprehensive_Design_Project.CUK_Compasser.domain.order.entity.OrderStatus;
import Comprehensive_Design_Project.CUK_Compasser.domain.order.repository.OrderRepository;
import Comprehensive_Design_Project.CUK_Compasser.domain.reward.converter.RewardConverter;
import Comprehensive_Design_Project.CUK_Compasser.domain.reward.dto.RewardSummary;
import Comprehensive_Design_Project.CUK_Compasser.domain.reward.repository.RewardRepository;
import Comprehensive_Design_Project.CUK_Compasser.global.common.apiPayload.code.status.ErrorStatus;
import Comprehensive_Design_Project.CUK_Compasser.global.common.apiPayload.exception.GeneralException;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;
    private final RewardRepository rewardRepository;
    private final OrderRepository orderRepository;
    private final RedisTemplate<String, String> redisTemplate;

    private static final String ADDRESS_KEY_PREFIX = "address:member:";

    @Value("${aws.host}") // 환경변수 주입 형식 수정 (${} 추가)
    private String AWS_HOST;

    public byte[] generateQRCode (Long memberId) {
        int width = 200, height = 200;
        BitMatrix encode = null;
        String memberIdJson = "{\"memberId\" : "+memberId.toString()+"}"; // JSON 형식 수정

        try {
            encode = new MultiFormatWriter().encode(memberIdJson, BarcodeFormat.QR_CODE, width, height);
        } catch (WriterException e) {
            throw new GeneralException(ErrorStatus.QR_IMAGE_WRITE_FAILED);
        }

        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(encode, "PNG", outputStream);
            return outputStream.toByteArray();
        } catch (Exception e) {
            log.error("Unexpected QR Generation Error: {}", e.getMessage());
            throw new GeneralException(ErrorStatus.QR_GENERATE_FAILED);
        }
    }

    public List<MemberRespDTO.RewardListDTO> getRewardList (Long memberId){
        memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));
        return RewardConverter.toRewardListDTO(rewardRepository.findAllByMember_Id(memberId));
    }

    @Transactional(readOnly = true)
    public MemberRespDTO.MyPageRespDTO getMyPageInfo(Long memberId) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("해당 회원을 찾을 수 없습니다."));

        RewardSummary rewardSummary = rewardRepository.getRewardSummaryByMemberId(memberId);
        Long totalUnboxing = orderRepository.countByMember_IdAndStatus(memberId, OrderStatus.PICKED_UP);

        return MemberRespDTO.MyPageRespDTO.builder()
                .memberName(member.getMemberName())
                .nickname(member.getNickname())
                .email(member.getEmail())
                .profileImageUrl(null) // 현재는 null 값
                .totalStampCount((int) rewardSummary.earnCount())   // long -> int
                .totalCouponCount((int) rewardSummary.couponCount()) // long -> int
                .totalUnboxingCount(totalUnboxing != null ? totalUnboxing.intValue() : 0)
                .build();
    }
}