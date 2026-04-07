package Comprehensive_Design_Project.CUK_Compasser.domain.owner.client;

import Comprehensive_Design_Project.CUK_Compasser.domain.owner.dto.NtsBusinessVerifyReqDTO;
import Comprehensive_Design_Project.CUK_Compasser.domain.owner.dto.NtsBusinessVerifyRespDTO;
import Comprehensive_Design_Project.CUK_Compasser.global.common.apiPayload.code.status.ErrorStatus;
import Comprehensive_Design_Project.CUK_Compasser.global.common.apiPayload.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class NtsBusinessVerifyClientImpl implements NtsBusinessVerifyClient {

    private final WebClient ntsWebClient;

    @Value("${nts.business.service-key}")
    private String serviceKey;

    @Override
    public boolean verify(String businessLicenseNumber, String startDate, String ownerName, String businessName) {

        NtsBusinessVerifyReqDTO request = NtsBusinessVerifyReqDTO.builder()
                .businesses(List.of(
                        NtsBusinessVerifyReqDTO.BusinessInfo.builder()
                                .b_no(businessLicenseNumber)
                                .start_dt(startDate)
                                .p_nm(ownerName)
                                .build()
                ))
                .build();

        try {
            NtsBusinessVerifyRespDTO response = ntsWebClient.post()
                    .uri(uriBuilder -> uriBuilder
                            .path("/validate")
                            .queryParam("serviceKey", serviceKey)
                            .queryParam("returnType", "JSON")
                            .build())
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(NtsBusinessVerifyRespDTO.class)
                    .block();

            if (response == null || response.getData() == null || response.getData().isEmpty()) {
                throw new GeneralException(ErrorStatus.BUSINESS_LICENSE_VERIFY_FAILED);
            }

            NtsBusinessVerifyRespDTO.BusinessStatus result = response.getData().get(0);

            log.info("NTS verify result - b_no: {}, valid: {}, valid_msg: {}",
                    result.getB_no(), result.getValid(), result.getValid_msg());

            return "01".equals(result.getValid());

        } catch (WebClientResponseException e) {
            log.error("NTS response error - status: {}, body: {}",
                    e.getStatusCode(), e.getResponseBodyAsString(), e);
            throw new GeneralException(ErrorStatus.BUSINESS_LICENSE_VERIFY_FAILED);

        } catch (WebClientRequestException e) {
            log.error("NTS request error - message: {}", e.getMessage(), e);
            throw new GeneralException(ErrorStatus.BUSINESS_LICENSE_VERIFY_FAILED);

        } catch (Exception e) {
            log.error("NTS verify unexpected error", e);
            throw new GeneralException(ErrorStatus.BUSINESS_LICENSE_VERIFY_FAILED);
        }
    }
}