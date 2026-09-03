package com.taskmanagement.aitaskmanagement.config;

import com.taskmanagement.aitaskmanagement.Redis.RateLimit.RateLimitProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(RateLimitProperties.class)
public class RateLimitConfig {
}
