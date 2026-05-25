package Comprehensive_Design_Project.CUK_Compasser.domain.store.dto.resp;

import Comprehensive_Design_Project.CUK_Compasser.domain.member.entity.BankType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreManagerInfoRespDTO {

    private String memberName;
    private String nickName;
    private String email;

    private BankType depositBankType;
    private String depositAccountNumber;
    private String depositAccountHolder;

    private String businessLicenseNumber;
}