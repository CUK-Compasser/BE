package Comprehensive_Design_Project.CUK_Compasser.domain.storeManager.controller;

import Comprehensive_Design_Project.CUK_Compasser.global.common.apiPayload.ApiResponse;
import Comprehensive_Design_Project.CUK_Compasser.global.security.userDetails.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/store_manager")
public class StoreManagerController {

    @GetMapping("/qr-check")
    @Operation(summary = "사장님 - QR 촬영", description = "사장님이 QR 찍었을 때 해당 사용자 정보 불러오는 API입니다.")
    public ApiResponse<Object> checkQR (){
        // 사장님이 QR 찍었을 때 사용자 정보 불러오는 API
        return null;
    }

    @PostMapping("/reward")
    public ApiResponse<Object> writingReward(@AuthenticationPrincipal CustomUserDetails userDetails){
        // 사장님의 적립 확인 버튼에 의한 실질적인 적립 로직
        return null;
    }
}
