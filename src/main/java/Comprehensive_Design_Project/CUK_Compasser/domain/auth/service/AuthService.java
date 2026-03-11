package Comprehensive_Design_Project.CUK_Compasser.domain.auth.service;

import Comprehensive_Design_Project.CUK_Compasser.domain.auth.dto.LoginReqDTO;
import Comprehensive_Design_Project.CUK_Compasser.domain.auth.dto.TokenRespDTO;
import Comprehensive_Design_Project.CUK_Compasser.domain.auth.entity.RefreshToken;
import Comprehensive_Design_Project.CUK_Compasser.domain.auth.repository.RefreshTokenRepository;
import Comprehensive_Design_Project.CUK_Compasser.global.security.jwt.JWT;
import Comprehensive_Design_Project.CUK_Compasser.global.security.jwt.JWTProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final AuthenticationManager authenticationManager; // SecurityConfig에 빈 등록 필요
    private final RefreshTokenRepository refreshTokenRepository;
    private final JWTProvider jwtProvider;

    @Transactional
    public TokenRespDTO login(LoginReqDTO request) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(request.email(), request.password());

        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        JWT jwt = jwtProvider.generateToken(authentication);

        RefreshToken refreshToken = RefreshToken.builder()
                .userId(authentication.getName())
                .refreshToken(jwt.getRefreshToken())
                .build();

        refreshTokenRepository.save(refreshToken);

        return TokenRespDTO.builder()
                .accessToken(jwt.getAccessToken())
                .refreshToken(jwt.getRefreshToken())
                .build();
    }
}