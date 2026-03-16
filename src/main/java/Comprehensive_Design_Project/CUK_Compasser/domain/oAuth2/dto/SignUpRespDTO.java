package Comprehensive_Design_Project.CUK_Compasser.domain.oAuth2.dto;

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
        private String memberName;
        private String email;
    }
}
