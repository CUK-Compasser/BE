package Comprehensive_Design_Project.CUK_Compasser.domain.oAuth2.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class KakaoLoginDTO {

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class KakaoLoginURLDTO {
        private String client_id;
        private String redirect_uri;
    }
}
