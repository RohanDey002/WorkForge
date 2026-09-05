package com.taskmanagement.aitaskmanagement.Redis.Presence;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;


@Setter
@Getter
@ConfigurationProperties(prefix = "app.presence")
public class PresenceProperties {

    private long onlineThresholdSeconds = 180;

    private long ttlDays = 30;
}
