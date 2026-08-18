package com.taskmanagement.aitaskmanagement.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Component
public class JWTAuthenticationEnttyPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException)
            throws IOException, ServletException {

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        response.setContentType("application/json");

        Map<String,Object> body = new HashMap<>();
        body.put("timestamp:", LocalDate.now().toString());
        body.put("status:",401);
        body.put("error:","unauthorized");
        body.put(
                "message",
                "Authentication is required to access this resource"
        );
        response.getWriter()
                .write(objectMapper.writeValueAsString(body));
    }

}
