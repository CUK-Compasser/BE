package Comprehensive_Design_Project.CUK_Compasser.global.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@RequiredArgsConstructor
public class KakaoLocalWebClientConfig {

    @Value("${kakao.local.base-url}")
    private String baseUrl;

    @Value("${kakao.local.rest-api-key}")
    private String restApiKey;

    @Bean
    public WebClient kakaoLocalWebClient(WebClient.Builder builder) {
        return builder
                .baseUrl(baseUrl)
                .defaultHeader("Authorization", "KakaoAK " + restApiKey.trim())
                .build();
    }
}