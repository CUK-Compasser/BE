package Comprehensive_Design_Project.CUK_Compasser.domain.store.dto.req;

import Comprehensive_Design_Project.CUK_Compasser.domain.member.entity.BankType;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AccountUpdateReqDTO {

    private BankType depositBankType;
    private String depositAccountNumber;
    private String depositAccountHolder;
}