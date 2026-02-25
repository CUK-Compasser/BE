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


}
