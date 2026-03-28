package Comprehensive_Design_Project.CUK_Compasser.domain.reservation.service;

import Comprehensive_Design_Project.CUK_Compasser.domain.reservation.dto.KakaoPayReqDTO;
import Comprehensive_Design_Project.CUK_Compasser.domain.reservation.dto.KakaoPayRespDTO;
import Comprehensive_Design_Project.CUK_Compasser.global.common.apiPayload.code.status.ErrorStatus;
import Comprehensive_Design_Project.CUK_Compasser.global.common.apiPayload.exception.GeneralException;
import Comprehensive_Design_Project.CUK_Compasser.global.config.KakaoPayProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Component
@RequiredArgsConstructor
public class KakaoPayClient {

    private final WebClient kakaoPayWebClient;
    private final KakaoPayProperties kakaoPayProperties;

    public KakaoPayRespDTO.ReadyResponseDTO ready(KakaoPayReqDTO.ReadyRequestDTO request) {
        try {
            log.info("[KakaoPayClient] ready 호출 시작 - url={}",
                    kakaoPayProperties.getBaseUrl() + "/online/v1/payment/ready");

            KakaoPayRespDTO.ReadyResponseDTO response = kakaoPayWebClient.post()
                    .uri(kakaoPayProperties.getBaseUrl() + "/online/v1/payment/ready")
                    .header(HttpHeaders.AUTHORIZATION, "SECRET_KEY " + kakaoPayProperties.getSecretKey())
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(KakaoPayRespDTO.ReadyResponseDTO.class)
                    .block(java.time.Duration.ofSeconds(10));

            if (response == null) {
                log.error("[KakaoPayClient] ready 응답이 null");
                throw new GeneralException(ErrorStatus.KAKAOPAY_READY_FAILED);
            }

            log.info("[KakaoPayClient] ready 호출 성공 - tid={}", response.getTid());
            return response;

        } catch (org.springframework.web.reactive.function.client.WebClientResponseException e) {
            log.error("[KakaoPayClient] ready 실패 - status={}, body={}",
                    e.getStatusCode(), e.getResponseBodyAsString(), e);
            throw new GeneralException(ErrorStatus.KAKAOPAY_READY_FAILED);
        } catch (Exception e) {
            log.error("[KakaoPayClient] ready 호출 실패", e);
            throw new GeneralException(ErrorStatus.KAKAOPAY_READY_FAILED);
        }
    }

    public KakaoPayRespDTO.ApproveResponseDTO approve(KakaoPayReqDTO.ApproveRequestDTO request) {
        try {
            return kakaoPayWebClient.post()
                    .uri(kakaoPayProperties.getBaseUrl() + "/online/v1/payment/approve")
                    .header(HttpHeaders.AUTHORIZATION, "SECRET_KEY " + kakaoPayProperties.getSecretKey())
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(KakaoPayRespDTO.ApproveResponseDTO.class)
                    .block();
        } catch (Exception e) {
            throw new GeneralException(ErrorStatus.KAKAOPAY_APPROVE_FAILED);
        }
    }
}