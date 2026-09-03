package com.taskmanagement.aitaskmanagement.Redis.RateLimit;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RateLimitService {

    private final StringRedisTemplate redisTemplate;
    private final RateLimitProperties rateLimitProperties;

    public boolean isAllowed( String key , RateLimitProperties.Limit limit){

        Long count = redisTemplate.opsForValue().increment(key);

        if(count==null) return false;

        if(count==1){
            redisTemplate.expire(key,
                    Duration.ofSeconds(limit.getWindowSeconds()));
        }

        return  count<= limit.getRequests();
    }


    public long getRemainingRequest(String key, RateLimitProperties.Limit limit){

        String value = redisTemplate.opsForValue().get(key);

        if(value==null) return limit.getRequests();

        try {
            long count = Long.parseLong(value);
            return Math.max(0, limit.getRequests()-count);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public  long retryAfterSecond(String key){
        Long ttl = redisTemplate.getExpire(key);

        if(ttl==null || ttl<=0) return 0;

        return ttl;

    }
}
