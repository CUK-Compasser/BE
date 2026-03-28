package Comprehensive_Design_Project.CUK_Compasser.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class KakaoPayWebClientConfig {

    @Bean
    public WebClient kakaoPayWebClient() {
        return WebClient.builder().build();
    }
}