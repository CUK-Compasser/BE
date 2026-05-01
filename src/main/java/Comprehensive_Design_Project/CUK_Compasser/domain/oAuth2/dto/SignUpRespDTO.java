package Comprehensive_Design_Project.CUK_Compasser.domain.oAuth2.dto;

import Comprehensive_Design_Project.CUK_Compasser.global.security.jwt.JWT;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class SignUpRespDTO {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class JoinRespDTO {
        private String accessToken;
        private String memberName;
        private String email;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class JoinRespDAO {
        private JWT jwt;
        private String memberName;
        private String email;
    }
}
