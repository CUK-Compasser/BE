package Comprehensive_Design_Project.CUK_Compasser.domain.member.dto;

import lombok.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

public class MemberReqDTO {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MemberCoordinatesDTO {
        private BigDecimal latitude;
        private BigDecimal longitude;
    }

    // erd 보고 수정
    @Getter
    @Setter
    @NoArgsConstructor
    public static class UpdateProfileDTO {
        private String memberName;
        private String profileImageUrl;
    }
}
