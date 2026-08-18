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

        if(authHeader==null|| !authHeader.startsWith("Bearer ")){
            filterChain.doFilter(request,response);
            return;
        }

        final  String jwt = authHeader.substring(7);

        String userEmail;

        try {

            userEmail=jwtService.extractUsername(jwt);
        }catch (Exception exception){

            filterChain.doFilter(request,response);
            return;
        }

        if(userEmail!=null&& SecurityContextHolder
                .getContext()
                .getAuthentication()==null){
            UserDetails userDetails;
            try {
                userDetails = userDetailsService.loadUserByUsername(userEmail);
            }catch (Exception exception){
                filterChain.doFilter(request,response);
                return;
            }

            if(jwtService.isTokenValid(jwt,userDetails)){
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

            }
        }


        filterChain.doFilter(request,response);
    }


}
