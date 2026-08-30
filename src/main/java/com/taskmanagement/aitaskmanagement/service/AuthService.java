package com.taskmanagement.aitaskmanagement.service;

import com.taskmanagement.aitaskmanagement.DTO.request.LoginRequest;
import com.taskmanagement.aitaskmanagement.DTO.response.AuthResult;
import com.taskmanagement.aitaskmanagement.DTO.response.JwtResponse;
import com.taskmanagement.aitaskmanagement.DTO.response.UserResponse;
import com.taskmanagement.aitaskmanagement.Redis.session.RedisSession;
import com.taskmanagement.aitaskmanagement.Redis.session.RedisSessionService;
import com.taskmanagement.aitaskmanagement.entity.User;
import com.taskmanagement.aitaskmanagement.exception.ResourceNotFoundException;
import com.taskmanagement.aitaskmanagement.exception.UnauthorizedException;
import com.taskmanagement.aitaskmanagement.repository.UserRepository;
import com.taskmanagement.aitaskmanagement.security.CustomUserDetails;
import com.taskmanagement.aitaskmanagement.security.JWTService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;
    private final UserRepository userRepository;
    private final RedisSessionService redisSessionService;

    public AuthResult login(LoginRequest request){

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );

        } catch (Exception e) {
            throw new UnauthorizedException("Email or Password is invalid");
        }

        User user = userRepository
                .findByEmail(request.getEmail())
                .orElseThrow(()->
                        new UnauthorizedException("User not found")
                );


        CustomUserDetails userDetails = new CustomUserDetails(user);

        long sessionStart = System.currentTimeMillis();

        String sessiondId = UUID.randomUUID().toString();

        String accessToken = jwtService.generateAccessToken(userDetails,sessionStart);

        String refreshToken  = jwtService.generateRefreshToken(userDetails,sessionStart,sessiondId);

        RedisSession session = RedisSession.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .sessionStart(sessionStart)
                .lastRefresh(sessionStart)
                .revoked(false)
                .build();

        Duration sessionExpiration = Duration.ofMillis(jwtService.getRefreshMaxExpiration());

        redisSessionService.createSession(sessiondId,session,sessionExpiration);


        UserResponse userResponse = mapToUserResponse(user);

        return  AuthResult.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .user(userResponse)
                .build();

    }


    public AuthResult refresh(String refreshToken){

        if(!jwtService.isRefreshTokenValid(refreshToken)){
            throw new UnauthorizedException("Invalid or expire refresh token");
        }

        String sessionId = jwtService.extractSessionId(refreshToken);

        if(sessionId==null) throw new UnauthorizedException("Session ID not found");

        RedisSession session = redisSessionService.getSession(sessionId)
                .orElseThrow(()->
                         new UnauthorizedException("Session not found"));

        if(session.isRevoked()) throw new UnauthorizedException("Session is revoked");




        String email = jwtService.extractUsername(refreshToken);

        User user = userRepository.findByEmail(email)
                .orElseThrow(()->
                        new ResourceNotFoundException("User not found with email :"+email));


        CustomUserDetails userDetails = new CustomUserDetails(user);

        Long sessionStart = jwtService.extractSessionStart(refreshToken);

        String newAccessToken = jwtService.generateAccessToken(userDetails,sessionStart);

        String newRefreshToken = jwtService.generateRefreshToken(userDetails,sessionStart,sessionId);


        session.setSessionStart(System.currentTimeMillis());

        long absoluteExpiration = sessionStart+ jwtService.getRefreshMaxExpiration();

        long remainingLifeTime = absoluteExpiration-System.currentTimeMillis();

        if(remainingLifeTime<=0) throw new UnauthorizedException("Maximum session is expired");

        redisSessionService.updateSession(sessionId,session,Duration.ofMillis(remainingLifeTime));


        UserResponse userResponse = mapToUserResponse(user);

        return AuthResult.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .user(userResponse)
                .build();


    }

    public Date getRefreshTokenExpiration(String refreshToken){

        return jwtService.extractExpiration(refreshToken);
    }

    private UserResponse mapToUserResponse(User user){
        Long managerId = null;
        if(user.getManager()!=null){
            managerId = user.getManager().getId();
        }

        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .managerId(managerId)
                .build();
    }
}
