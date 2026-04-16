package Comprehensive_Design_Project.CUK_Compasser.domain.member.service;

import Comprehensive_Design_Project.CUK_Compasser.domain.member.dto.MemberRespDTO;
import Comprehensive_Design_Project.CUK_Compasser.domain.member.entity.Member;
import Comprehensive_Design_Project.CUK_Compasser.domain.member.repository.MemberRepository;
import Comprehensive_Design_Project.CUK_Compasser.domain.reward.dto.RewardEachStoreRecord;
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
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;


@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;
    private final RewardRepository rewardRepository;
    private final RedisTemplate<String, String> redisTemplate;

    private static final String ADDRESS_KEY_PREFIX = "address:member:";
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    public static String generateVerificationCode() {
        Random random = new Random();
        StringBuilder code = new StringBuilder(4);

        for (int i = 0; i < 4; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            code.append(CHARACTERS.charAt(randomIndex));
        }

        return code.toString();
    }

    public byte[] generateQRCode (Long memberId) {
        int width = 200, height = 200;
        BitMatrix encode = null;
        String qrToken = generateVerificationCode();
        String memberDataJson = "{\"memberId\" : "+memberId.toString()+", \"token\": "  + "\""+ qrToken +"\""+"}"; // JSON 형식 수정
        redisTemplate.opsForValue().set("qr:" + memberId, qrToken, 65, TimeUnit.SECONDS);

        try {
            encode = new MultiFormatWriter().encode(memberDataJson, BarcodeFormat.QR_CODE, width, height);
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

    @Transactional(readOnly = true)
    public List<RewardEachStoreRecord> getRewardList (Long memberId){
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));
        return rewardRepository.findAllByMember_Id(memberId);
    }

    @Transactional(readOnly = true)
    public MemberRespDTO.MyPageRespDTO getMyPageInfo(Long memberId) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("해당 회원을 찾을 수 없습니다."));

        RewardSummary rewardSummary = rewardRepository.getRewardSummaryByMemberId(memberId);

        return MemberRespDTO.MyPageRespDTO.builder()
                .memberName(member.getMemberName())
                .nickname(member.getNickname())
                .email(member.getEmail())
                .profileImageUrl(null) // 현재는 null 값
                .totalStampCount((int) rewardSummary.earnCount())   // long -> int
                .totalCouponCount((int) rewardSummary.couponCount()) // long -> int
                .build();
    }
}