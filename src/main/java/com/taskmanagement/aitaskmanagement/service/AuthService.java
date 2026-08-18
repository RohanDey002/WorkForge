package com.taskmanagement.aitaskmanagement.service;

import com.taskmanagement.aitaskmanagement.DTO.request.LoginRequest;
import com.taskmanagement.aitaskmanagement.DTO.response.JwtResponse;
import com.taskmanagement.aitaskmanagement.entity.User;
import com.taskmanagement.aitaskmanagement.exception.UnauthorizedException;
import com.taskmanagement.aitaskmanagement.repository.UserRepository;
import com.taskmanagement.aitaskmanagement.security.CustomUserDetails;
import com.taskmanagement.aitaskmanagement.security.JWTService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;
    private final UserRepository userRepository;

    public JwtResponse login(LoginRequest request){

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

        String token = jwtService.generateToke( new CustomUserDetails(user));


        return JwtResponse.builder()
                .token(token)
                .type("Bearer")
                .email(user.getEmail())
                .role(user.getRole().name())
                .build();

    }
}
