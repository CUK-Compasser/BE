package Comprehensive_Design_Project.CUK_Compasser.domain.reservation.controller;

import Comprehensive_Design_Project.CUK_Compasser.global.common.properties.AppProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/payments/kakaopay")
public class KakaoPayCallbackController {

    private final AppProperties appProperties;

    @GetMapping("/success")
    public ResponseEntity<Void> success(
            @RequestParam Long reservationId,
            @RequestParam("pg_token") String pgToken
    ) {
        URI redirectUri = URI.create(
                appProperties.getFrontendBaseUrl() +
                        "/payment/success?reservationId=" + reservationId + "&pg_token=" + pgToken
        );

        return ResponseEntity.status(302)
                .header(HttpHeaders.LOCATION, redirectUri.toString())
                .build();
    }

    @GetMapping("/cancel")
    public ResponseEntity<Void> cancel(@RequestParam Long reservationId) {
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
        URI redirectUri = URI.create(
                appProperties.getFrontendBaseUrl() +
                        "/payment/fail?reservationId=" + reservationId
        );

        return ResponseEntity.status(302)
                .header(HttpHeaders.LOCATION, redirectUri.toString())
                .build();
    }
}