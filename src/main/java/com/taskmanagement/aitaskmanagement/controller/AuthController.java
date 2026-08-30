package com.taskmanagement.aitaskmanagement.controller;

import com.taskmanagement.aitaskmanagement.DTO.request.LoginRequest;
import com.taskmanagement.aitaskmanagement.DTO.response.AuthResponse;
import com.taskmanagement.aitaskmanagement.DTO.response.AuthResult;
import com.taskmanagement.aitaskmanagement.DTO.response.JwtResponse;
import com.taskmanagement.aitaskmanagement.Redis.session.RedisSessionService;
import com.taskmanagement.aitaskmanagement.Util.CookiesUtil;
import com.taskmanagement.aitaskmanagement.exception.UnauthorizedException;
import com.taskmanagement.aitaskmanagement.security.JWTService;
import com.taskmanagement.aitaskmanagement.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final CookiesUtil cookiesUtil;
    private final JWTService jwtService;
    private final RedisSessionService redisSessionService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request){

        AuthResult result = authService.login(request);

        ResponseCookie accessCookie = cookiesUtil.createAccessTokenCookie(result.getAccessToken());

        Duration refreshCookieLifeTime = calculateExpirationTime(result.getRefreshToken());

        ResponseCookie refreshCookie = cookiesUtil.createRefreshToken(result.getRefreshToken(),
                refreshCookieLifeTime);

        AuthResponse response = AuthResponse.builder()
                .message("Login successfully")
                .user(result.getUser())
                .build();

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.SET_COOKIE,
                        accessCookie.toString()
                )
                .header(HttpHeaders.SET_COOKIE,
                        refreshCookie.toString()
                        )
                .body(response);


    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(HttpServletRequest request){

        String refresh = extractRefreshToken(request);

        if(refresh==null) throw  new UnauthorizedException("Refresh token not found");

        AuthResult result = authService.refresh(refresh);

        ResponseCookie accessCookie = cookiesUtil.createAccessTokenCookie(result.getAccessToken());

        Duration refreshCookieLifeTime = calculateExpirationTime(result.getRefreshToken());

        ResponseCookie refreshCookie = cookiesUtil.createRefreshToken(result.getRefreshToken(),
                refreshCookieLifeTime);

        AuthResponse response = AuthResponse.builder()
                .message("Token refreshed successfully")
                .user(result.getUser())
                .build();

        return  ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE,
                        accessCookie.toString())
                .header(HttpHeaders.SET_COOKIE,
                        refreshCookie.toString())
                .body(response);

    }

    @PostMapping("/logout")
    public ResponseEntity<AuthResponse> logout(HttpServletRequest request){

        String refreshToken  = extractRefreshToken(request);

        if(refreshToken!=null){

            try {
                String sessionId = jwtService.extractSessionId(refreshToken);

                redisSessionService.revokeSession(sessionId);

            }catch (Exception exception){

            }

        }


        ResponseCookie accessCookie = cookiesUtil.deleteAccessToken();

        ResponseCookie responseCookie = cookiesUtil.deleteRefreshToken();

        AuthResponse response = AuthResponse.builder()
                .message("Logout")
                .user(null)
                .build();

       return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE,
                        accessCookie.toString())
                .header(HttpHeaders.SET_COOKIE,
                        responseCookie.toString())
                .body(response);
    }

    private String extractRefreshToken(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();

        if(cookies==null) return null;

        return Arrays.stream(cookies)
                .filter(
                        cookie -> "refresh_token"
                                .equals(cookie.getName()
                                )
                )
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);
    }

    private Duration calculateExpirationTime(String refreshToken){

        return Duration.between(
                Instant.now(),
                authService.getRefreshTokenExpiration(refreshToken)
                        .toInstant()
        );
    }
}
