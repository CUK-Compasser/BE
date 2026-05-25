package Comprehensive_Design_Project.CUK_Compasser.domain.oAuth2.service;

import Comprehensive_Design_Project.CUK_Compasser.domain.member.dto.MemberRespDTO;
import Comprehensive_Design_Project.CUK_Compasser.domain.member.entity.Login;
import Comprehensive_Design_Project.CUK_Compasser.domain.member.entity.Member;
import Comprehensive_Design_Project.CUK_Compasser.domain.member.entity.MemberRole;
import Comprehensive_Design_Project.CUK_Compasser.domain.member.entity.Role;
import Comprehensive_Design_Project.CUK_Compasser.domain.member.repository.MemberRepository;
import Comprehensive_Design_Project.CUK_Compasser.domain.oAuth2.dto.LoginReqDTO;
import Comprehensive_Design_Project.CUK_Compasser.domain.oAuth2.dto.SignUpReqDTO;
import Comprehensive_Design_Project.CUK_Compasser.domain.oAuth2.dto.SignUpRespDTO;
import Comprehensive_Design_Project.CUK_Compasser.domain.oAuth2.dto.TokenRespDTO;
import Comprehensive_Design_Project.CUK_Compasser.global.common.apiPayload.code.status.ErrorStatus;
import Comprehensive_Design_Project.CUK_Compasser.global.common.apiPayload.exception.GeneralException;
import Comprehensive_Design_Project.CUK_Compasser.global.security.jwt.JWT;
import Comprehensive_Design_Project.CUK_Compasser.global.security.jwt.JWTProvider;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class OAuth2Service {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String client_id;

    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String redirect_uri;

    private final JWTProvider jwtProvider;
    private final RedisTemplate<String, String> redisTemplate;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public SignUpRespDTO.JoinRespDAO joinMember(SignUpReqDTO.JoinReqDTO joinRequestDTO) {

        // 이메일 중복 확인
        if (memberRepository.findByEmail(joinRequestDTO.getEmail()).isPresent()) {
            throw new RuntimeException("이미 존재하는 이메일입니다.");
        }

        // 비밀번호 일치 확인
        if(!joinRequestDTO.getPassword().equals(joinRequestDTO.getPasswordConfirm())) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }

        String encodedPassword = passwordEncoder.encode(joinRequestDTO.getPassword());

        Member newMember = Member.builder()
                .email(joinRequestDTO.getEmail())
                .password(encodedPassword)
                .memberName(joinRequestDTO.getMemberName())
                .nickname(joinRequestDTO.getNickname())
                // 2. provider를 LOCAL로 수정 (엔티티 정의와 일치)
                .provider(Login.NORMAL)
                .role(MemberRole.NORMAL)
                .status(Role.ACTIVE)
                .build();

        Member savedMember = memberRepository.save(newMember);

        // 2. API 플로우 한계로 인한, 회원가입 시에도 JWT 발급 (점장 승격 API와 플로우 충돌)
        List<SimpleGrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(newMember.getRole().toString()));
        Authentication authentication = new UsernamePasswordAuthenticationToken(newMember.getEmail(), newMember.getPassword(), authorities);

        SignUpRespDTO.JoinRespDAO dao = SignUpRespDTO.JoinRespDAO.builder()
                .jwt(jwtProvider.generateToken(authentication))
                .memberName(newMember.getMemberName())
                .email(newMember.getEmail())
                .build();

        return dao;
/*
        // 3. 빌더를 통해 생성된 객체 반환
        return SignUpRespDTO.JoinRespDTO.builder()
                .memberName(savedMember.getMemberName())
                .email(savedMember.getEmail())
                .build();*/
    }

    // 일반 로그인 서비스
    @Transactional
    public TokenRespDTO login(LoginReqDTO request) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(request.email(), request.password());

        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        Member member = memberRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        JWT jwt = jwtProvider.generateToken(authentication);

        redisTemplate.opsForValue().set(
                "refresh:" + authentication.getName(),
                jwt.getRefreshToken(),
                7,
                TimeUnit.DAYS
        );

        return TokenRespDTO.builder()
                .accessToken(jwt.getAccessToken())
                .refreshToken(jwt.getRefreshToken())
                .role(member.getRole())
                .build();
    }

    // JWT
    @Transactional
    public MemberRespDTO.MemberInfoDTO loginWithKakao(String code){
        log.info("[OAuth2Service] - Received code: {}", code);

        // set Header
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // set body
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", client_id);
        body.add("redirect_uri", redirect_uri);
        body.add("code", code);

        // spring -> kakao oauth server
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(body, headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );

        // 받은 정보 처리
        String responseBody = response.getBody();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = null;
        try {
            jsonNode = mapper.readTree(responseBody);
        } catch (JsonProcessingException e) {
            log.info("[OAuth2Service] - Error parsing response: {}", responseBody);
            throw new RuntimeException(e);
        }

        String accessToken = jsonNode.get("access_token").asText();
        log.info("[OAuth2Service] - kakao 에서 받은 accessToken: {}", accessToken);
//        log.info("--------------");
//        log.info("accessToken -> kakaoUserInfo 요청");


        // set headers
        HttpHeaders headers2 = new HttpHeaders();
        headers2.add("Content-Type","application/x-www-form-urlencoded;charset=UTF-8");
        headers2.add("Authorization","Bearer "+accessToken);

        // set body
        MultiValueMap<String, String> body2 = new LinkedMultiValueMap<>(); // 없으면 401 에러 발생. null body 라도 넣어줘야 함
        HttpEntity <MultiValueMap<String, String>> kakaoTokenRequest2 = new HttpEntity<>(body2, headers2);

        // 받은 토큰으로 사용자 정보 얻기, spring -> kakao oauth server
        RestTemplate restTemplate2 = new RestTemplate();
        ResponseEntity<String> response2 = restTemplate2.exchange("https://kapi.kakao.com/v2/user/me", HttpMethod.POST, kakaoTokenRequest2, String.class);

        String responseBody2 = response2.getBody();
        ObjectMapper mapper2 = new ObjectMapper();
        JsonNode jsonNode2 = null;
        try {
            jsonNode2 = mapper2.readTree(responseBody2);
        } catch (JsonProcessingException e) {
            log.warn("null !!!!!");
            throw new RuntimeException(e);
        }


        // 사용자 정보 처리
        String kakaoId = jsonNode2.get("id").asText();
        String memberName = jsonNode2.get("properties").get("nickname").asText();
        String email = jsonNode2.get("kakao_account").get("email").asText();

        Optional<Member> memberOptional = memberRepository.findByProviderAndProviderId(Login.KAKAO, kakaoId);
        if(memberOptional.isEmpty()) {
            // 없는 경우 저장
            memberRepository.save(Member.createNewMemberByKakao(email, memberName, kakaoId));
            memberOptional = memberRepository.findByProviderAndProviderId(Login.KAKAO, kakaoId);
        }
        Member member = memberOptional.get();


        List<SimpleGrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(member.getRole().toString()));
        Authentication authentication = new UsernamePasswordAuthenticationToken(member.getEmail(), member.getPassword(), authorities);
        JWT jwt = jwtProvider.generateToken(authentication);

        redisTemplate.opsForValue().set("refresh:"+member.getEmail(), jwt.getRefreshToken());

//        log.info("kakaoUserInfo: {}", jsonNode2.toPrettyString());
//        log.info("username: {}", memberName);
//        log.info("userId (email): {}", email);

        return MemberRespDTO.MemberInfoDTO.builder().jwt(jwt).memberName(memberName).role(member.getRole()).build();
    }

    public void logout (String accessToken, Long memberId){
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));
        redisTemplate.delete("refresh:"+member.getEmail());

        Long remainingTime = jwtProvider.getRemainingTime(accessToken); // TTL 용 남은 시간 계산

        if (remainingTime > 0) {
            String key = "blacklist:" + accessToken;
            redisTemplate.opsForValue().set(key, "logout", remainingTime, TimeUnit.MILLISECONDS);
            log.info("[MemberService] - add AccessToken in BlackList! remainingTime: {}ms", remainingTime);
        }

        log.info("[MemberService - logout] Success to logout -> {}", member.getEmail());
    }
}
