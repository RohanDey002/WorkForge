package com.taskmanagement.aitaskmanagement.config;

import com.taskmanagement.aitaskmanagement.security.CustomUserDetailsService;
import com.taskmanagement.aitaskmanagement.security.JWTService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JWTService jwtService;
    private final CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        final  String authHeader = request.getHeader("AUTHORIZATION");

        System.out.println("Authorization Header:"+ authHeader);

        if(authHeader==null|| !authHeader.startsWith("Bearer ")){
            System.out.println("No Bearer token found");
            filterChain.doFilter(request,response);
            return;
        }

        final  String jwt = authHeader.substring(7);
        System.out.println("JWT received");

        String userEmail;

        try {

            userEmail=jwtService.extractUsername(jwt);
            System.out.println(
                    "Username extracted from JWT: " + userEmail
            );
        }catch (Exception exception){
            System.out.println(
                    "JWT extraction failed: "
                            + exception.getMessage()
            );
            exception.printStackTrace();

            filterChain.doFilter(request,response);
            return;
        }

        if(userEmail!=null&& SecurityContextHolder
                .getContext()
                .getAuthentication()==null){
            UserDetails userDetails;
            try {
                userDetails = userDetailsService.loadUserByUsername(userEmail);
                System.out.println(
                        "User loaded: "
                                + userDetails.getUsername()
                );
            }catch (Exception exception){
                System.out.println(
                        "User loading failed: "
                                + exception.getMessage()
                );

                exception.printStackTrace();
                filterChain.doFilter(request,response);
                return;
            }

            if(jwtService.isTokenValid(jwt,userDetails)){
                System.out.println("JWT IS VALID");
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails,
                                null,
                                userDetails.getAuthorities());
                authenticationToken.setDetails(
                        new WebAuthenticationDetailsSource()
                                .buildDetails(request)
                );

                SecurityContextHolder
                        .getContext()
                        .setAuthentication(authenticationToken);
                System.out.println(
                        "Authentication set: "
                                + SecurityContextHolder
                                .getContext()
                                .getAuthentication()
                );

                System.out.println(
                        "Authorities: "
                                + authenticationToken.getAuthorities()
                );

            }else {
                System.out.println("JWT IS INVALID");
            }
        }


        filterChain.doFilter(request,response);
    }


}
