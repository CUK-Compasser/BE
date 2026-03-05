package Comprehensive_Design_Project.CUK_Compasser.global.integration.kakao.kakao.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class KakaoAddressSearchRespDTO {

    private List<Document> documents;

    @Getter
    @NoArgsConstructor
    public static class Document {

        private String x;
        private String y;

        private Address address;

        @JsonProperty("road_address")
        private RoadAddress roadAddress;
    }

    @Getter
    @NoArgsConstructor
    public static class Address {
        @JsonProperty("address_name")
        private String addressName;
    }

    @Getter
    @NoArgsConstructor
    public static class RoadAddress {
        @JsonProperty("address_name")
        private String addressName;
    }
}