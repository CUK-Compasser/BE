package Comprehensive_Design_Project.CUK_Compasser.domain.owner.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class NtsBusinessVerifyRespDTO {

    private Integer request_cnt;
    private List<BusinessStatus> data;

    @Getter
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class BusinessStatus {
        private String b_no;
        private String valid;
        private String valid_msg;
    }
}