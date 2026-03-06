package Comprehensive_Design_Project.CUK_Compasser.global.integration.kakao.kakao.service;

import Comprehensive_Design_Project.CUK_Compasser.global.common.apiPayload.code.status.ErrorStatus;
import Comprehensive_Design_Project.CUK_Compasser.global.common.apiPayload.exception.GeneralException;
import Comprehensive_Design_Project.CUK_Compasser.global.integration.kakao.kakao.dto.KakaoAddressSearchRespDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoLocalService {

    private final WebClient kakaoLocalWebClient;

    public KakaoAddressSearchRespDTO.Document searchAddress(String inputAddress) {
        try {
            KakaoAddressSearchRespDTO response = kakaoLocalWebClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/v2/local/search/address.json")
                            .queryParam("query", inputAddress)
                            .build())
                    .retrieve()
                    .bodyToMono(KakaoAddressSearchRespDTO.class)
                    .block();

            if (response == null || response.getDocuments() == null || response.getDocuments().isEmpty()) {
                throw new GeneralException(ErrorStatus.STORE_ADDRESS_NOT_FOUND);
            }

            return response.getDocuments().get(0);

        } catch (WebClientResponseException e) {
            log.error("[KakaoLocalService] status={}, body={}",
                    e.getStatusCode(), e.getResponseBodyAsString(), e);
            throw new GeneralException(ErrorStatus.STORE_LOCATION_CONVERT_FAILED);
        } catch (Exception e) {
            log.error("[KakaoLocalService] unexpected error", e);
            throw new GeneralException(ErrorStatus.STORE_LOCATION_CONVERT_FAILED);
        }
    }
}