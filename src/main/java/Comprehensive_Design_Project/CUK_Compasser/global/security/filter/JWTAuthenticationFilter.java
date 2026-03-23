package Comprehensive_Design_Project.CUK_Compasser.global.security.filter;

import Comprehensive_Design_Project.CUK_Compasser.global.common.apiPayload.code.generalStatus.GeneralErrorCode;
import Comprehensive_Design_Project.CUK_Compasser.global.security.jwt.JWT;
import Comprehensive_Design_Project.CUK_Compasser.global.security.jwt.JWTProvider;
import Comprehensive_Design_Project.CUK_Compasser.global.security.userDetails.CustomUserDetailsService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@Slf4j
@RequiredArgsConstructor
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    private final JWTProvider jwtProvider;
    private final RedisTemplate redisTemplate;
    private final CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String accessToken = resolveAccessToken(request);

        try {
            if (accessToken != null && jwtProvider.validateToken(accessToken)) {
                String isBlackListed = (String) redisTemplate.opsForValue().get("blacklist:" + accessToken);
                if (isBlackListed != null) {
                    log.warn("[JWTAuthenticationFilter] - Using BlackListed Token!");
                    // 직접 응답을 보내고 필터 체인을 종료합니다.
                    sendErrorResponse(response, GeneralErrorCode.BLACKLIST_TOKEN);
                    return;
                }

                Authentication authentication = jwtProvider.getAuthentication(accessToken);
                if (authentication != null) {
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        } catch (ExpiredJwtException e) {
            log.warn("[JWTAuthenticationFilter] - AT expired, attempting to refresh token");
            handleExpiredAccessToken(request, response, e);
            return; // 갱신 로직에서 응답을 이미 보냈으므로 종료
        } catch (JwtException | IllegalArgumentException e) {
            log.info("[JWTAuthenticationFilter] - Invalid Token! ");
            // 필요 시 여기서도 에러 응답을 보낼 수 있습니다.
        }

        filterChain.doFilter(request, response);
    }

    private String resolveAccessToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7).trim();
        }
        return null;
    }

    private String resolveRefreshToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) return null;

        return Arrays.stream(cookies)
                .filter(cookie -> "refreshToken".equals(cookie.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElse(null);
    }

    private void handleExpiredAccessToken(HttpServletRequest request, HttpServletResponse response, ExpiredJwtException e) throws IOException {
        Claims claims = e.getClaims();
        String username = claims.getSubject();

        String requestRT = resolveRefreshToken(request);
        String savedRT = (String) redisTemplate.opsForValue().get("refresh:" + username);

        if (savedRT == null || requestRT == null || !requestRT.equals(savedRT)) {
            log.warn("[JWTAuthenticationFilter] - RT invalid or missing!");
            sendErrorResponse(response, GeneralErrorCode.RT_NOT_FOUND);
            return;
        }

        // RT가 유효한 경우 새로운 토큰 발급
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        JWT newJWT = jwtProvider.generateToken(authentication);

        response.setHeader("Authorization", "Bearer " + newJWT.getAccessToken());
        ResponseCookie cookie = ResponseCookie.from("refreshToken", newJWT.getRefreshToken())
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(7 * 24 * 60 * 60)
                .sameSite("Strict")
                .build();
        response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        redisTemplate.opsForValue().set("refresh:" + username, newJWT.getRefreshToken());

        SecurityContextHolder.getContext().setAuthentication(authentication);
        log.info("Successfully refreshed token for user: {}", username);

        // 토큰 갱신 후에도 요청을 계속 진행하려면 filterChain.doFilter를 호출해야 하지만,
        // 보통은 헤더만 실어서 401 대신 200으로 처리하거나 클라이언트에게 다시 요청하라고 안내합니다.
    }

    private void sendErrorResponse(HttpServletResponse response, GeneralErrorCode errorCode) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        String body = String.format(
                "{\"isSuccess\": false, \"code\": \"%s\", \"message\": \"%s\"}",
                errorCode.getCode(),
                errorCode.getMessage()
        );

        response.getWriter().write(body);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        return path.startsWith("/actuator")
                || path.startsWith("/swagger-ui")
                || path.startsWith("/v3/api-docs")
                || path.startsWith("/favicon.ico");
    }
}