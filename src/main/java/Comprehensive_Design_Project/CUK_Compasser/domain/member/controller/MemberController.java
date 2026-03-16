package Comprehensive_Design_Project.CUK_Compasser.domain.member.controller;

import Comprehensive_Design_Project.CUK_Compasser.domain.member.dto.MemberRespDTO;
import Comprehensive_Design_Project.CUK_Compasser.domain.member.service.MemberService;
import Comprehensive_Design_Project.CUK_Compasser.global.common.apiPayload.ApiResponse;
import Comprehensive_Design_Project.CUK_Compasser.global.common.apiPayload.code.status.SuccessStatus;
import Comprehensive_Design_Project.CUK_Compasser.global.security.userDetails.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;


    /*@GetMapping("/qr")
    public ApiResponse<Object> getRewardQR (@AuthenticationPrincipal CustomUserDetails userDetails){
        return ApiResponse.onSuccess(SuccessStatus.OK, memberService.generateQRCode(userDetails.getMember().getId()));
    }*/


    /*
    * 브라우저가 인식하려면, Content-Type 필요해서....
    */
    @GetMapping(value = "/qr/test", produces = MediaType.IMAGE_PNG_VALUE)
    @Operation(summary = "QR 코드 이미지 직접 확인용 (테스트용)", description = "브라우저에서 바로 이미지를 볼 수 있도록 하는 테스트용 API")
    public ResponseEntity<Resource> getRewardQRTest(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Resource resource = new ByteArrayResource(memberService.generateQRCode(userDetails.getMember().getId()));
//        log.info("bytes = {}", bytes);
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG) // 브라우저에게 이미지임을 알림
                .body(resource);
    }

    @GetMapping("/reward")
    @Operation(summary = "적립 현황 확인 API", description = "사용자가 여러 가게에서 적립한 현황을 확인하는 API 입니다.")
    public ApiResponse<List<MemberRespDTO.RewardListDTO>> getRewardList (@AuthenticationPrincipal CustomUserDetails userDetails) {
        return ApiResponse.onSuccess(SuccessStatus.OK, memberService.getRewardList(userDetails.getMember().getId()));
    }

    @GetMapping("/my-page")
    @Operation(summary = "마이페이지 조회 API", description = "로그인한 사용자의 프로필 및 적립 통계를 조회합니다.")
    public ApiResponse<MemberRespDTO.MyPageRespDTO> getMyPage (@AuthenticationPrincipal CustomUserDetails userDetails) {

        return ApiResponse.onSuccess(SuccessStatus.OK, memberService.getMyPageInfo(userDetails.getMember().getId()));
    }
}
