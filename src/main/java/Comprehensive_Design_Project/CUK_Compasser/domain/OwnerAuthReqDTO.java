package Comprehensive_Design_Project.CUK_Compasser.domain;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

public class OwnerAuthReqDTO {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BusinessLicenseVerifyReqDTO {
        @NotBlank
        private String businessLicenseNumber;

        // 명세에서 email? 이라 optional
        private String email;
    }
}