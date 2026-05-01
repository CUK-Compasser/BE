package Comprehensive_Design_Project.CUK_Compasser.domain.oAuth2.controller;

import Comprehensive_Design_Project.CUK_Compasser.domain.member.dto.MemberRespDTO;
import Comprehensive_Design_Project.CUK_Compasser.domain.member.service.MemberService;
import Comprehensive_Design_Project.CUK_Compasser.domain.oAuth2.dto.LoginReqDTO;
import Comprehensive_Design_Project.CUK_Compasser.domain.oAuth2.dto.SignUpReqDTO;
import Comprehensive_Design_Project.CUK_Compasser.domain.oAuth2.dto.SignUpRespDTO;
import Comprehensive_Design_Project.CUK_Compasser.domain.oAuth2.dto.TokenRespDTO;
import Comprehensive_Design_Project.CUK_Compasser.global.common.apiPayload.ApiResponse;
import Comprehensive_Design_Project.CUK_Compasser.global.common.apiPayload.code.generalStatus.GeneralErrorCode;
import Comprehensive_Design_Project.CUK_Compasser.global.common.apiPayload.code.status.ErrorStatus;
import Comprehensive_Design_Project.CUK_Compasser.global.common.apiPayload.code.status.SuccessStatus;
import Comprehensive_Design_Project.CUK_Compasser.domain.oAuth2.service.OAuth2Service;
import Comprehensive_Design_Project.CUK_Compasser.global.common.apiPayload.exception.GeneralException;
import Comprehensive_Design_Project.CUK_Compasser.global.security.userDetails.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/oauth2")
public class OAuth2Controller {

    private final MemberService memberService;
    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String client_id;

    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String redirect_uri;

    private final OAuth2Service oAuth2Service;

    public record AccessTokenDTO(String accessToken) {}

    @PostMapping("/sign-up")
    @Operation(summary = "일반 회원가입 API", description = "이름, 닉네임, 이메일 등을 입력받아 회원가입을 진행합니다.")
    public ApiResponse<SignUpRespDTO.JoinRespDTO> signUp(HttpServletResponse response,@RequestBody @Valid SignUpReqDTO.JoinReqDTO signUpReqDTO) {

        SignUpRespDTO.JoinRespDAO joinRespDAO = oAuth2Service.joinMember(signUpReqDTO);

        ResponseCookie cookie = ResponseCookie.from("refreshToken", joinRespDAO.getJwt().getRefreshToken())
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(7 * 24 * 60 * 60) // 7일
                .sameSite("Strict")
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return ApiResponse.onSuccess(SuccessStatus.CREATED, SignUpRespDTO.JoinRespDTO.builder()
                .email(joinRespDAO.getEmail())
                .accessToken(joinRespDAO.getJwt().getAccessToken())
                .memberName(joinRespDAO.getEmail()).build());
    }


    @PostMapping("/login")
    @Operation(summary = "일반 로그인 API", description = "이메일과 비밀번호를 사용하여 로그인을 진행합니다.")
    public ApiResponse<AccessTokenDTO> login(
            @Valid @RequestBody LoginReqDTO request,
            HttpServletResponse response
    ) {
        TokenRespDTO tokenResponse = oAuth2Service.login(request);

        ResponseCookie cookie = ResponseCookie.from("refreshToken", tokenResponse.refreshToken())
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(7 * 24 * 60 * 60) // 7일
                .sameSite("Strict")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return ApiResponse.onSuccess(SuccessStatus.OK, new AccessTokenDTO(tokenResponse.accessToken()));
    }

    @PostMapping("/login-kakao")
    @Operation(summary = "카카오 로그인 URL 획득 API", description = "사용자가 이동할 카카오 로그인 동의 페이지 URL를 획득하는 API 입니다.")
    public ApiResponse<String> loginKakao(){
        return ApiResponse.onSuccess(SuccessStatus.OK, "https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=" +  client_id + "&redirect_uri=" + redirect_uri);
    }

    @GetMapping("/code/kakao") // 카카오 회원가입 or 로그인 서비스 == GET 요청
    @Operation(summary = "카카오 로그인 API", description = "사용자가 카카오 로그인 동의 후 받는 API 입니다. 카카오 서버 인증 후 받은 memberName과 함께 " +
            "https://compasser.co.kr/auth/callback?accessToken={accessToken} 로 리다이렉트 하는 API 입니다.")
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

        try {
            response.sendRedirect("https://compasser.co.kr/auth/callback?accessToken=".concat(memberInfo.getJwt().getAccessToken()) );
        } catch (IOException e) {
            throw new GeneralException(GeneralErrorCode.WRONG_REDIRECT_URL);
        }

        MemberRespDTO.LoginRespDTO respDTO = MemberRespDTO.LoginRespDTO.builder().isSuccess(true).memberName(memberInfo.getMemberName()).accessToken(memberInfo.getJwt().getAccessToken()).build();
        return ApiResponse.onSuccess(SuccessStatus.OK, respDTO);
    }

    @PostMapping("/logout")
    @Operation(summary = "로그아웃 API", description = "로그인을 한 사용자에 한해, 로그아웃을 진행하는 API 입니다.")
    public ApiResponse<Object> logout(HttpServletRequest request, @AuthenticationPrincipal CustomUserDetails userDetails) {

        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            oAuth2Service.logout(bearerToken.substring(7).trim(), userDetails.getMember().getId());
        }
        return ApiResponse.onSuccess(SuccessStatus.OK); // bool 값 리턴
    }
}
