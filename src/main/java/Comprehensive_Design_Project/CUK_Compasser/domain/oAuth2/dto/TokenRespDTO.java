package Comprehensive_Design_Project.CUK_Compasser.domain.oAuth2.dto;

import lombok.Builder;

@Builder
public record TokenRespDTO (
        String accessToken,
        String refreshToken
) { }
