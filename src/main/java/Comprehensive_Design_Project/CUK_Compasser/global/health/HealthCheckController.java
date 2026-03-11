package Comprehensive_Design_Project.CUK_Compasser.global.health;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {

    @GetMapping("/health")
    public String healthCheck() {
        return "OK"; // 로드밸런서가 200 OK를 받으면 Healthy로 판단합니다.
    }
}
