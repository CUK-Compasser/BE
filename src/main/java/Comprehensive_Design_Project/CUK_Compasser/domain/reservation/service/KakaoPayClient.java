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
import org.springframework.web.reactive.function.client.WebClientResponseException;

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
            log.info("[KakaoPayClient] approve 호출 시작 - url={}, cid={}, tid={}, partnerOrderId={}, partnerUserId={}, pgToken={}",
                    kakaoPayProperties.getBaseUrl() + "/online/v1/payment/approve",
                    request.getCid(),
                    request.getTid(),
                    request.getPartner_order_id(),
                    request.getPartner_user_id(),
                    request.getPg_token());

            KakaoPayRespDTO.ApproveResponseDTO response = kakaoPayWebClient.post()
                    .uri(kakaoPayProperties.getBaseUrl() + "/online/v1/payment/approve")
                    .header(HttpHeaders.AUTHORIZATION, "SECRET_KEY " + kakaoPayProperties.getSecretKey())
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(KakaoPayRespDTO.ApproveResponseDTO.class)
                    .block(java.time.Duration.ofSeconds(10));

            if (response == null) {
                log.error("[KakaoPayClient] approve 응답이 null");
                throw new GeneralException(ErrorStatus.KAKAOPAY_APPROVE_FAILED);
            }

            log.info("[KakaoPayClient] approve 호출 성공 - tid={}, partnerOrderId={}, approvedAt={}",
                    response.getTid(), response.getPartner_order_id(), response.getApproved_at());

            return response;

        } catch (WebClientResponseException e) {
            log.error("[KakaoPayClient] approve 실패 - status={}, body={}",
                    e.getStatusCode(), e.getResponseBodyAsString(), e);
            throw new GeneralException(ErrorStatus.KAKAOPAY_APPROVE_FAILED);

        } catch (Exception e) {
            log.error("[KakaoPayClient] approve 호출 중 예외", e);
            throw new GeneralException(ErrorStatus.KAKAOPAY_APPROVE_FAILED);
        }
    }

    public KakaoPayRespDTO.CancelRespDTO cancel(KakaoPayReqDTO.CancelReqDTO request) {
        try {
            log.info("[KakaoPayClient] cancel 호출 시작 - url={}, cid={}, tid={}, cancelAmount={}",
                    kakaoPayProperties.getBaseUrl() + "/online/v1/payment/cancel",
                    request.getCid(),
                    request.getTid(),
                    request.getCancel_amount());

            KakaoPayRespDTO.CancelRespDTO response = kakaoPayWebClient.post()
                    .uri(kakaoPayProperties.getBaseUrl() + "/online/v1/payment/cancel")
                    .header(HttpHeaders.AUTHORIZATION, "SECRET_KEY " + kakaoPayProperties.getSecretKey())
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(KakaoPayRespDTO.CancelRespDTO.class)
                    .block(java.time.Duration.ofSeconds(10));

            if (response == null) {
                log.error("[KakaoPayClient] cancel 응답이 null");
                throw new GeneralException(ErrorStatus.KAKAOPAY_CANCEL_FAILED);
            }

            log.info("[KakaoPayClient] cancel 호출 성공 - tid={}", response.getTid());
            return response;

        } catch (WebClientResponseException e) {
            log.error("[KakaoPayClient] cancel 실패 - status={}, body={}",
                    e.getStatusCode(), e.getResponseBodyAsString(), e);
            throw new GeneralException(ErrorStatus.KAKAOPAY_CANCEL_FAILED);

        } catch (Exception e) {
            log.error("[KakaoPayClient] cancel 호출 중 예외", e);
            throw new GeneralException(ErrorStatus.KAKAOPAY_CANCEL_FAILED);
        }
    }
}