package Comprehensive_Design_Project.CUK_Compasser.domain.member.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OwnerBusinessVerifyReqDTO {

    @NotBlank
    private String businessLicenseNumber; // 사업자등록번호

    @NotBlank
    private String ownerName; // 대표자명

    @NotBlank
    private String startDate; // 개업일자 yyyyMMdd

    private String businessName; // 상호명 (선택)
}