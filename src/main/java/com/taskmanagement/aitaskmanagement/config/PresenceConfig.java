package com.taskmanagement.aitaskmanagement.config;

import com.taskmanagement.aitaskmanagement.Redis.Presence.PresenceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(PresenceProperties.class)
public class PresenceConfig {
}
