package Comprehensive_Design_Project.CUK_Compasser.domain.owner.dto;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BusinessLicenseVerifyRespDTO {
    private Boolean isValid;
    private String businessLicenseNumber;
    private String message; // "검증 성공" 같은 프론트 메시지용
}