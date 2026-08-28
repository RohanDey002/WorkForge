package com.taskmanagement.aitaskmanagement.Util;


import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class CookiesUtil {

    private static final  String ACCESS_TOKEN = "access_token";
    private static final String REFRESH_TOKEN = "refresh_token";

    public ResponseCookie createAccessTokenCookie(String token){

        return ResponseCookie
                .from(ACCESS_TOKEN,token)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(Duration.ofMinutes(15))
                .sameSite("Lax")
                .build();
    }

    public ResponseCookie createRefreshToken(String token, Duration maxage){

        return ResponseCookie
                .from(REFRESH_TOKEN,token)
                .httpOnly(true)
                .secure(false)
                .path("api/auth")
                .maxAge(maxage)
                .sameSite("Lax")
                .build();
    }

    public ResponseCookie deleteAccessToken(){

        return ResponseCookie
                .from(ACCESS_TOKEN,"")
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(Duration.ZERO)
                .sameSite("Lax")
                .build();
    }

    public ResponseCookie deleteRefreshToken(){

        return ResponseCookie
                .from(REFRESH_TOKEN,"")
                .httpOnly(true)
                .secure(false)
                .path("api/auth")
                .maxAge(Duration.ZERO)
                .sameSite("Lax")
                .build();
    }
}
