package Comprehensive_Design_Project.CUK_Compasser.domain.member.dto;

import Comprehensive_Design_Project.CUK_Compasser.global.security.jwt.JWT;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class MemberRespDTO {
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MemberInfoDTO {
        private JWT jwt;
        private String memberName;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LoginRespDTO {
        private Boolean isSuccess;
        private String memberName;
        private String accessToken;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class QRDTO {
        private Long memberId;
        private String token; // redis 인증용 토큰
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RewardListDTO {
        private Long rewardId;
        private Integer points;
        private String storeName;
    }

    // erd 보고 수정
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MyPageRespDTO {
        private String memberName;
        private String nickname;
        private String email;
        private String profileImageUrl;

        private Integer totalStampCount; // 적립 횟수
        private Integer totalUnboxingCount; // 랜덤박스 언박싱 횟수
        private Integer totalCouponCount; // 쿠폰 사용 횟수
    }
}
