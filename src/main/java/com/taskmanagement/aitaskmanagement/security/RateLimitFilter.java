package com.taskmanagement.aitaskmanagement.security;


import com.taskmanagement.aitaskmanagement.Redis.RateLimit.RateLimitProperties;
import com.taskmanagement.aitaskmanagement.Redis.RateLimit.RateLimitService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class RateLimitFilter extends OncePerRequestFilter {

    private final RateLimitService rateLimitService;
    private final RateLimitProperties rateLimitProperties;
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String requestPath = request.getRequestURI();

        if(shouldSkip(requestPath)){
            filterChain.doFilter(request,response);
            return;
        }

        RateLimitProperties.Limit limit;
        String key;

        if("/api/auth/login".equals(requestPath) && "POST".equalsIgnoreCase(request.getMethod())){

            limit = rateLimitProperties.getLogin();
            key = "rate:limit:login:" + clientIP(request);

        } else if("/api/auth/refresh".equals(requestPath) && "POST".equalsIgnoreCase(request.getMethod())){

            limit = rateLimitProperties.getRefresh();
            key = "rate:limit:refresh:" + clientIP(request);
        }else {

            Authentication authentication =
                    SecurityContextHolder
                            .getContext()
                            .getAuthentication();

            if(authentication==null || !authentication.isAuthenticated()){
                filterChain.doFilter(request,response);
                return;

            }
            Object principle = authentication.getPrincipal();

            if(!(principle instanceof  CustomUserDetails userDetails)){
                filterChain.doFilter(request,response);
                return;
            }

            limit = rateLimitProperties.getAuthenticated();
            key = "rate:limit:user:" + userDetails.getId();

        }

         boolean allowed = rateLimitService.isAllowed(key,limit);

        long remaining = rateLimitService.getRemainingRequest(key,limit);

        response.setHeader(
                "X-RateLimit-Limit",
                String.valueOf(limit.getRequests()));
        response.setHeader("X-RateLimit-Remaining",
                String.valueOf(Math.max(0,remaining)));

        if(!allowed){

            long retryAfter = rateLimitService.retryAfterSecond(key);

            response.setStatus(
                    HttpStatus.TOO_MANY_REQUESTS.value()
            );

            response.setContentType("application/json");

            response.setHeader("Retry-after",String.valueOf(retryAfter));

            Map<String, Object> body =
                    new LinkedHashMap<>();


            body.put(
                    "error",
                    "Too Many Requests"
            );

            body.put(
                    "message",
                    "Rate limit exceeded. Please try again later."
            );

            body.put(
                    "retryAfterSeconds",
                    retryAfter
            );

            response.getWriter().write(
                    objectMapper.writeValueAsString(body)
            );
            return;

        }

        filterChain.doFilter(request,response);


    }

    private boolean shouldSkip(String requestPath){

        return requestPath.equals("/api/auth/logout");
    }

    private String clientIP(HttpServletRequest request){

        String forwardedFor = request.getHeader("X-Forwarded-For");

        if(forwardedFor!=null && !forwardedFor.isBlank()){
            return forwardedFor
                    .split(",")[0]
                    .trim();
        }

        return request.getRemoteAddr();

    }
}
