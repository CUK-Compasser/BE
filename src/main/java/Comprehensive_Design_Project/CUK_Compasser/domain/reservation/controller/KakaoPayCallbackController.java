package Comprehensive_Design_Project.CUK_Compasser.domain.reservation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/payments/kakaopay")
public class KakaoPayCallbackController {

    @GetMapping("/success")
    public ResponseEntity<Void> success(
            @RequestParam Long reservationId,
            @RequestParam("pg_token") String pgToken
    ) {
        URI redirectUri = URI.create(
                "http://localhost:3000/payment/success?reservationId=" + reservationId + "&pg_token=" + pgToken
        );

        return ResponseEntity.status(302)
                .header(HttpHeaders.LOCATION, redirectUri.toString())
                .build();
    }

    @GetMapping("/cancel")
    public ResponseEntity<Void> cancel(@RequestParam Long reservationId) {
        URI redirectUri = URI.create(
                "http://localhost:3000/payment/cancel?reservationId=" + reservationId
        );

        return ResponseEntity.status(302)
                .header(HttpHeaders.LOCATION, redirectUri.toString())
                .build();
    }

    @GetMapping("/fail")
    public ResponseEntity<Void> fail(@RequestParam Long reservationId) {
        URI redirectUri = URI.create(
                "http://localhost:3000/payment/fail?reservationId=" + reservationId
        );

        return ResponseEntity.status(302)
                .header(HttpHeaders.LOCATION, redirectUri.toString())
                .build();
    }
}