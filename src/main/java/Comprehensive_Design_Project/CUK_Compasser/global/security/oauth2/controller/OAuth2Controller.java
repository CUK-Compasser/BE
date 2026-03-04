package Comprehensive_Design_Project.CUK_Compasser.global.security.oAuth2.controller;

import Comprehensive_Design_Project.CUK_Compasser.domain.member.dto.MemberRespDTO;
import Comprehensive_Design_Project.CUK_Compasser.global.common.apiPayload.ApiResponse;
import Comprehensive_Design_Project.CUK_Compasser.global.common.apiPayload.code.status.SuccessStatus;
import Comprehensive_Design_Project.CUK_Compasser.global.security.oAuth2.dto.KakaoLoginDTO;
import Comprehensive_Design_Project.CUK_Compasser.global.security.oAuth2.service.OAuth2Service;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(("/api/oauth2"))
public class OAuth2Controller {

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String client_id;

    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String redirect_uri;

    private final OAuth2Service oAuth2Service;

    @PostMapping("/login-kakao")
    public ApiResponse<String> loginKakao(){
        return ApiResponse.onSuccess(SuccessStatus.OK, "https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=" +  client_id + "&redirect_uri=" + redirect_uri);
    }

    @GetMapping("/code/kakao") // 카카오 회원가입 or 로그인 서비스 == GET 요청
    public ApiResponse<Object> callback (@RequestParam("code") String code, HttpServletResponse response) {

        MemberRespDTO.MemberInfoDTO memberInfo = oAuth2Service.loginWithKakao(code);

        ResponseCookie cookie = ResponseCookie.from("refreshToken", memberInfo.getJwt().getRefreshToken())
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(7 * 24 * 60 * 60) // 7일
                .sameSite("Strict")
                .build();
        response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        MemberRespDTO.LoginRespDTO respDTO = MemberRespDTO.LoginRespDTO.builder().isSuccess(true).memberName(memberInfo.getMemberName()).accessToken(memberInfo.getJwt().getAccessToken()).build();
        return ApiResponse.onSuccess(SuccessStatus.OK, respDTO);
    }
}
