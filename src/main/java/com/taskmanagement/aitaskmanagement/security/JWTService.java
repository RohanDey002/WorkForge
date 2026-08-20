package com.taskmanagement.aitaskmanagement.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.function.Function;

@Service
public class JWTService {

    private  String secrete_key = "this888is999the777login666keyFORTHEPROJECTTASKMANAGEMENT";

    private  long expiration = 86400000;

    public  String generateToke(UserDetails userDetails){
        return Jwts.builder()
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis()+expiration))
                .signWith(getSigningKey(),Jwts.SIG.HS256)
                .compact();
    }

    public String extractUsername(String token){

        return extractClaim(token,Claims::getSubject);
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

    public  Date extractExpiration(String token){
        return extractClaim(token,Claims::getExpiration);
    }

    public boolean  isTokenExpired(String token){
        return extractExpiration(token)
                .before(new Date());
    }

    public boolean isTokenValid(String token, UserDetails userDetails){

        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);

    }


    private SecretKey getSigningKey(){
        byte[] keyBytes = Decoders.BASE64.decode(secrete_key);

        return Keys.hmacShaKeyFor(keyBytes);
    }
}
