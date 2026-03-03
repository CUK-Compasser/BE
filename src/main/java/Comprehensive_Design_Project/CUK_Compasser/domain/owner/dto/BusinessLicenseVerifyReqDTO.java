package Comprehensive_Design_Project.CUK_Compasser.domain.owner.dto;

import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BusinessLicenseVerifyReqDTO {
    private String businessLicenseNumber;
    private String email; // 선택: 명세에 email? 로 되어있음
}