package Comprehensive_Design_Project.CUK_Compasser.domain.auth.dto;

import lombok.Builder;

@Builder
public record TokenRespDTO (
        String accessToken,
        String refreshToken
) {}
