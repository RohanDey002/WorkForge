package com.taskmanagement.aitaskmanagement.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.function.Function;

@Service
public class JWTService {

    private  String secrete_key = "this888is999the777login666keyFORTHEPROJECTTASKMANAGEMENT";

    private  long accessTokenExpiration = 900000;
    private  long refreshIdleExpiration = 604800000;
    private  long refreshMaxExpiration =  1296000000;

    public String generateAccessToken(UserDetails userDetails , long sessionStart){

        long currentTime = System.currentTimeMillis();

        long absoluteExpiration = sessionStart + refreshMaxExpiration;

        long accessExpirationTime = Math.min(currentTime+accessTokenExpiration,absoluteExpiration);

        return Jwts.builder()
                .subject(userDetails.getUsername())
                .claim("type","ACCESS")
                .claim("sessionStart",sessionStart)
                .issuedAt(new Date(currentTime))
                .expiration(new Date( accessExpirationTime))
                .signWith(getSigningKey(),Jwts.SIG.HS256)
                .compact();

    }

    public  String generateRefreshToken(UserDetails userDetails , long sessionStart){
        long currentTime = System.currentTimeMillis();

        long absoluteExpiration = sessionStart+refreshMaxExpiration;

        long refreshExpirationTime = Math.min(currentTime+refreshIdleExpiration,absoluteExpiration);

        return Jwts.builder()
                .subject(userDetails.getUsername())
                .claim("type","REFRESH")
                .claim("sessionStart",sessionStart)
                .issuedAt(new Date(currentTime))
                .expiration(new Date(refreshExpirationTime))
                .signWith(getSigningKey(),Jwts.SIG.HS256)
                .compact();

    }

    public String generateInitialRefreshToken(UserDetails userDetails){
        long currentTime  = System.currentTimeMillis();

        return generateRefreshToken(userDetails,currentTime);
    }



    public <T> T extractClaim(String token, Function< Claims,T> ClaimsResolver){

        Claims  claims = extractAllClaims(token);

        return ClaimsResolver.apply(claims);
    }

    public Claims extractAllClaims(String token){
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public  String extractUsername(String token){

        return  extractClaim(token,Claims::getSubject);
    }

    public  String extractTokenType(String token){
        return extractClaim(token,
                claims -> claims.get("type",String.class));
    }

    public Long  extractSessionStart(String token){

        return extractClaim(token,
                claims -> claims.get("sessionStart", Long.class));
    }

    public  Date extractExpiration(String token){

        return extractClaim(token,Claims::getExpiration);
    }

    public boolean  isTokenExpired(String token){
        return extractExpiration(token)
                .before(new Date());
    }

    public boolean isAccessTokenValid(String token, UserDetails userDetails){
        try {
            String username = extractUsername(token);
            String tokentype = extractTokenType(token);

            return (username.equals(userDetails.getUsername())
            && "ACCESS".equals(tokentype) && !isTokenExpired(token));

        }catch (Exception exception){
            return false;
        }
    }

    public  boolean isRefreshTokenValid(String token ){

        try {

            String tokeType = extractTokenType(token);

            Long sessionStart  = extractSessionStart(token);

            if(!"REFRESH".equals(tokeType)) return false;

            if(sessionStart==null)  return false;

            if(System.currentTimeMillis() >= sessionStart+refreshMaxExpiration) return false;

            if(isTokenExpired(token)) return false;

            return true;
        }catch (Exception exception){
            return false;
        }
    }


    private SecretKey getSigningKey(){

        return Keys.hmacShaKeyFor(secrete_key.getBytes(StandardCharsets.UTF_8));
    }
}
