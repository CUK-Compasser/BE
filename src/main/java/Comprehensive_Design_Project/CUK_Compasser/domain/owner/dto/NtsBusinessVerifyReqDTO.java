package Comprehensive_Design_Project.CUK_Compasser.domain.owner.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NtsBusinessVerifyReqDTO {
    private List<BusinessInfo> businesses;

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BusinessInfo {
        private String b_no;
        private String start_dt;
        private String p_nm;
    }
}