package com.taskmanagement.aitaskmanagement.config;

import com.taskmanagement.aitaskmanagement.Redis.Presence.PresenceServices;
import com.taskmanagement.aitaskmanagement.security.CustomUserDetails;
import com.taskmanagement.aitaskmanagement.security.CustomUserDetailsService;
import com.taskmanagement.aitaskmanagement.security.JWTService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
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
    private final PresenceServices presenceServices;


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String jwt = extractAccessCookie(request);

        if(jwt==null){

            filterChain.doFilter(request,response);
            return;
        }

        try {

            String email  = jwtService.extractUsername(jwt);
            if(email!=null && SecurityContextHolder.getContext().getAuthentication()==null){

                UserDetails userDetails = userDetailsService.loadUserByUsername(email);

                if(jwtService.isAccessTokenValid(jwt,userDetails)){

                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );

                    authenticationToken.setDetails(
                            new WebAuthenticationDetailsSource()
                                    .buildDetails(request)
                    );

                    SecurityContextHolder.getContext()
                            .setAuthentication(authenticationToken);

                    if(userDetails instanceof CustomUserDetails customUserDetails){
                        presenceServices.recordLastActive(customUserDetails.getId());
                    }
                }
            }

        } catch (Exception exception) {

            exception.printStackTrace();
            SecurityContextHolder
                    .clearContext();

        }

        filterChain.doFilter(request,response);


    }

    private String extractAccessCookie(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();

        if(cookies==null) return null;

        for(Cookie cookie : cookies){
            if ("access_token".equals(cookie.getName())){
                return cookie.getValue();
            }
        }
        return null;
    }
}
