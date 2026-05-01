package Comprehensive_Design_Project.CUK_Compasser.domain.reservation.controller;

import Comprehensive_Design_Project.CUK_Compasser.domain.reservation.service.ReservationService;
import Comprehensive_Design_Project.CUK_Compasser.global.common.properties.AppProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/payments/kakaopay")
public class KakaoPayCallbackController {

    private final AppProperties appProperties;
    private final ReservationService reservationService;

    @GetMapping("/success")
    public ResponseEntity<Void> success(
            @RequestParam Long reservationId,
            @RequestParam("pg_token") String pgToken
    ) {
        try {
            reservationService.approveKakaoPayByCallback(reservationId, pgToken);

            URI redirectUri = URI.create(
                    appProperties.getFrontendBaseUrl() +
                            "/payment/success?reservationId=" + reservationId
            );
            return ResponseEntity.status(302)
                    .header(HttpHeaders.LOCATION, redirectUri.toString())
                    .build();

        } catch (Exception e) {
            log.error("[KakaoPayCallback] 결제 승인 실패 - reservationId={}", reservationId, e);

            URI redirectUri = URI.create(
                    appProperties.getFrontendBaseUrl() +
                            "/payment/fail?reservationId=" + reservationId
            );
            return ResponseEntity.status(302)
                    .header(HttpHeaders.LOCATION, redirectUri.toString())
                    .build();
        }
    }

    @GetMapping("/cancel")
    public ResponseEntity<Void> cancel(@RequestParam Long reservationId) {
        try {
            reservationService.cancelKakaoPayByCallback(reservationId);
        } catch (Exception e) {
            log.warn("[KakaoPayCallback] 취소 처리 실패 - reservationId={}", reservationId, e);
        }

        URI redirectUri = URI.create(
                appProperties.getFrontendBaseUrl() +
                        "/payment/cancel?reservationId=" + reservationId
        );
        return ResponseEntity.status(302)
                .header(HttpHeaders.LOCATION, redirectUri.toString())
                .build();
    }

    @GetMapping("/fail")
    public ResponseEntity<Void> fail(@RequestParam Long reservationId) {
        try {
            reservationService.markPaymentFailedByCallback(reservationId);
        } catch (Exception e) {
            log.warn("[KakaoPayCallback] 실패 처리 중 예외 - reservationId={}", reservationId, e);
        }

        URI redirectUri = URI.create(
                appProperties.getFrontendBaseUrl() +
                        "/payment/fail?reservationId=" + reservationId
        );
        return ResponseEntity.status(302)
                .header(HttpHeaders.LOCATION, redirectUri.toString())
                .build();
    }
}