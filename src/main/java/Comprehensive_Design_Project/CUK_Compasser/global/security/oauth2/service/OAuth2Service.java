package Comprehensive_Design_Project.CUK_Compasser.global.security.oAuth2.service;

import Comprehensive_Design_Project.CUK_Compasser.domain.member.dto.MemberRespDTO;
import Comprehensive_Design_Project.CUK_Compasser.domain.member.entity.Member;
import Comprehensive_Design_Project.CUK_Compasser.domain.member.repository.MemberRepository;
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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class OAuth2Service {

    private final MemberRepository memberRepository;
    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String client_id;

    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String redirect_uri;

    private final JWTProvider jwtProvider;
    private final RedisTemplate redisTemplate;

    // JWT
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
        String memberName = jsonNode2.get("properties").get("nickname").asText();
        String email = jsonNode2.get("kakao_account").get("email").asText();


//        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        Optional<Member> memberOptional = memberRepository.findByEmail(email);
        if(memberOptional.isEmpty()) {
            // 없는 경우 저장
            memberRepository.save(Member.createNewMemberByKakao(email, memberName));
            memberOptional = memberRepository.findByEmail(email);
        }
        Member member = memberOptional.get();


        List<SimpleGrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(member.getRole().toString()));
        Authentication authentication = new UsernamePasswordAuthenticationToken(member.getEmail(), member.getPassword(), authorities);
        JWT jwt = jwtProvider.generateToken(authentication);

        redisTemplate.opsForValue().set("refresh:"+member.getEmail(), jwt.getRefreshToken());

//        log.info("kakaoUserInfo: {}", jsonNode2.toPrettyString());
//        log.info("username: {}", memberName);
//        log.info("userId (email): {}", email);

        // 여기 추가, 통합
        return MemberRespDTO.MemberInfoDTO.builder().jwt(jwt).memberName(memberName).build();
    }
}
