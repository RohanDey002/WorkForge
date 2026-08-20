package com.taskmanagement.aitaskmanagement.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JWTAuthenticationEnttyPoint jwtAuthenticationEnttyPoint;

    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{

        http
                .csrf(csrt->csrt.disable())
                .cors(cors->cors.configurationSource(
                        corsConfigurationSource()
                ))
                .sessionManagement(session->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        ))
                .authenticationProvider(
                        authenticationProvider
                )
                .authorizeHttpRequests(
                        auth->auth
                                .requestMatchers(
                                        "/api/auth/**"
                                ).permitAll()

                                .requestMatchers(
                                        "/swagger-ui/**",
                                        "/v3/api-docs/**"
                                ).permitAll()

                                .requestMatchers(
                                        "/api/admin/**"
                                ).hasRole("Admin")

                                .requestMatchers(
                                        "/api/manager/**"
                                ).hasRole("Manager")

                                .requestMatchers(
                                        "/api/employee/**"
                                ).hasRole("Employee")

                                .anyRequest()
                                .authenticated()
                )
                .exceptionHandling(exception->
                        exception.authenticationEntryPoint(
                                jwtAuthenticationEnttyPoint
                        ))
                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

       return http.build();



    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration corsConfiguration = new CorsConfiguration();

        corsConfiguration.setAllowedOrigins(
                List.of("http://localhost:5173")
        );
        corsConfiguration.setAllowedMethods(
                List.of(
                        "GET",
                        "POST",
                        "PUT",
                        "DELETE",
                        "PATCH",
                        "OPTIONS"
                )
        );
        corsConfiguration.setAllowedHeaders(List.of("*"));

        corsConfiguration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source  = new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**",
                corsConfiguration);

        return source;
    }


}
