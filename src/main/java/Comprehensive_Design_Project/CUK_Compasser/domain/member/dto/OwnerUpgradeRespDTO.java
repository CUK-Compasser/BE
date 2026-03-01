package Comprehensive_Design_Project.CUK_Compasser.domain.member.dto;

import Comprehensive_Design_Project.CUK_Compasser.domain.member.entity.MemberRole;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OwnerUpgradeRespDTO {
    private Long memberId;
    private MemberRole role;
    private Long storeId;
    private Boolean alreadyUpgraded; // 이미 점장이었는지(멱등)
}