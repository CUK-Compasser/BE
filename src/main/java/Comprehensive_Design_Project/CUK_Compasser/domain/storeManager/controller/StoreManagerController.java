package Comprehensive_Design_Project.CUK_Compasser.domain.storeManager.controller;

import Comprehensive_Design_Project.CUK_Compasser.domain.member.dto.MemberRespDTO;
import Comprehensive_Design_Project.CUK_Compasser.domain.storeManager.dto.req.StoreManagerReqDTO;
import Comprehensive_Design_Project.CUK_Compasser.domain.storeManager.dto.resp.GetMemberRewardRecord;
import Comprehensive_Design_Project.CUK_Compasser.domain.storeManager.dto.resp.StoreManagerRespDTO;
import Comprehensive_Design_Project.CUK_Compasser.domain.storeManager.service.StoreManagerService;
import Comprehensive_Design_Project.CUK_Compasser.global.common.apiPayload.ApiResponse;
import Comprehensive_Design_Project.CUK_Compasser.global.common.apiPayload.code.status.SuccessStatus;
import Comprehensive_Design_Project.CUK_Compasser.global.security.userDetails.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/store_manager")
public class StoreManagerController {

    private final StoreManagerService storeManagerService;

    @PostMapping("/qr-check")
    @Operation(summary = "사장님 - QR 촬영", description = "사장님이 QR 찍었을 때 해당 사용자 정보 불러오는 API입니다.")
    public ApiResponse<GetMemberRewardRecord> checkingQR (
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam String token,
            @RequestParam Long memberId
            /*@RequestBody MemberRespDTO.QRDTO qrDTO*/){
        // 사장님이 QR 찍었을 때 사용자 정보 불러오는 API

        return ApiResponse.onSuccess(SuccessStatus.OK, storeManagerService.checkingQR(userDetails.getMember().getId(), token, memberId));
    }

    @PostMapping("/reward")
    @Operation(summary = "사장님 - QR 촬영", description = "사장님이 QR로 조회한 사용자에 대해 적립을 허용하는 API 입니다.")
    public ApiResponse<Object> writingReward(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody StoreManagerReqDTO.WritingRewardDTO dto){
        // 사장님의 적립 확인 버튼에 의한 실질적인 적립 로직
        storeManagerService.writingReward(dto);
        return ApiResponse.onSuccess(SuccessStatus.OK);
    }
}
