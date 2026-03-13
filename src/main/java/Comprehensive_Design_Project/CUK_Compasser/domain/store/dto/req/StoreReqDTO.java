package Comprehensive_Design_Project.CUK_Compasser.domain.store.dto.req;

import Comprehensive_Design_Project.CUK_Compasser.domain.store.entity.Tag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

public class StoreReqDTO {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class StoreReqWithCoordinateDTO {
        private BigDecimal latitude;
        private BigDecimal longitude;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class StoreReqWithCoordinateAndTagDTO {
        private BigDecimal latitude;
        private BigDecimal longitude;
        private Tag tag;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class StoreReqWithCoordinateAndUniversityDTO {
        private BigDecimal latitude;
        private BigDecimal longitude;
        private String university;
    }
}
