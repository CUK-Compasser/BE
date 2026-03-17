package Comprehensive_Design_Project.CUK_Compasser.domain.member.service;

import Comprehensive_Design_Project.CUK_Compasser.domain.member.dto.MemberRespDTO;
import Comprehensive_Design_Project.CUK_Compasser.domain.member.entity.Member;
import Comprehensive_Design_Project.CUK_Compasser.domain.member.repository.MemberRepository;
import Comprehensive_Design_Project.CUK_Compasser.domain.order.entity.OrderStatus;
import Comprehensive_Design_Project.CUK_Compasser.domain.order.repository.OrderRepository;
import Comprehensive_Design_Project.CUK_Compasser.domain.reward.converter.RewardConverter;
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

    private static final String ADDRESS_KEY_PREFIX = "address:member";

    @Value("aws.host")
    private String AWS_HOST;

    public byte[] generateQRCode (Long memberId) {
        int width = 200, height = 200;

        BitMatrix encode = null;
        String memberIdJson = "{memberId : "+memberId.toString()+"}";
        try {
            encode = new MultiFormatWriter().encode(memberIdJson, BarcodeFormat.QR_CODE, width, height);
        } catch (WriterException e) {
            throw new GeneralException(ErrorStatus.QR_IMAGE_WRITE_FAILED); // 새로 에러 코드 만들기
        }

        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(encode, "PNG", outputStream);
            return outputStream.toByteArray(); // qr 코드는 일회용, 발급만, 필요에 따라 저장
        } catch (IllegalArgumentException e) {
            // 가로/세로가 0 이하일 때 등
            throw new GeneralException(ErrorStatus.QR_INVALID_SIZE);
        } catch (Exception e) {
            // 기타 예상치 못한 오류
            log.error("Unexpected QR Generation Error: {}", e.getMessage());
            throw new GeneralException(ErrorStatus.QR_GENERATE_FAILED);
        }
    }

    public List<MemberRespDTO.RewardListDTO> getRewardList (Long memberId){
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        // rewardRepository -> 각 필드와 store_id 갖고 오기 -> store_id를 통한 store 이름 조회 필요
        // DTO 변환, return
        return RewardConverter.toRewardListDTO(rewardRepository.findAllByMember_Id(memberId));
    }

    @Transactional(readOnly = true)
    public MemberRespDTO.MyPageRespDTO getMyPageInfo(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("해당 회원을 찾을 수 없습니다."));

        Integer totalStamp = rewardRepository.sumTotalStampsByMemberId(memberId);
        Integer totalUsedCoupon = rewardRepository.sumTotalUsedCouponsByMemberId(memberId);

        Long totalUnboxing = orderRepository.countByMember_IdAndStatus(memberId, OrderStatus.PICKED_UP);

        return MemberRespDTO.MyPageRespDTO.builder()
                .memberName(member.getMemberName())
                .nickname(member.getNickname())
                .email(member.getEmail())
                .profileImageUrl(null) // 현재 엔티티에 프로필 이미지 필드가 없으므로 null
                .totalStampCount(totalStamp != null ? totalStamp : 0)
                .totalUnboxingCount(totalUnboxing != null ? totalUnboxing.intValue(): 0)
                .totalCouponCount(totalUsedCoupon != null ? totalUsedCoupon : 0)
                .build();
    }
}
