package Comprehensive_Design_Project.CUK_Compasser.domain.oAuth2.dto;

import Comprehensive_Design_Project.CUK_Compasser.domain.member.entity.MemberRole;
import lombok.Builder;

@Builder
public record TokenRespDTO (
        String accessToken,
        String refreshToken,
        MemberRole role
) { }
