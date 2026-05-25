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
public class AccountUpdateRespDTO {

    private BankType depositBankType;
    private String depositAccountNumber;
    private String depositAccountHolder;
}