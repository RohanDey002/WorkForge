package com.taskmanagement.aitaskmanagement.Redis.RateLimit;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;




@Getter
@Setter
@ConfigurationProperties(prefix = "app.rate-limit")
public class RateLimitProperties {

    private Limit login = new Limit();
    private Limit refresh =new Limit();
    private Limit authenticated = new Limit();


    @Getter
    @Setter
    public static class Limit{
        private int requests;
        private long windowSeconds;
    }
}
