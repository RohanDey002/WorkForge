package com.taskmanagement.aitaskmanagement.Redis.Presence;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class PresenceServices {
    private static final String KEY_PREFIX ="presence:user:";

    private final StringRedisTemplate redisTemplate;
    private final PresenceProperties properties;


    public void  recordLastActive(Long userId){

        String key =  buildKey(userId);

        Long now = System.currentTimeMillis();

        redisTemplate.opsForValue().set(
                key,
                String.valueOf(now),
                Duration.ofDays(properties.getTtlDays())
        );

    }

    public Long getLastSeen(Long userId){

        String key = buildKey(userId);

        String value = redisTemplate.opsForValue().get(key);

        if(value== null) return null;

        try {

            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            throw new RuntimeException(e);
        }
    }

    public String getLastActive(Long userId){
        Long lastSeen = getLastSeen(userId);

        if(lastSeen==null) return "Last Active : Unknown";

        long now = System.currentTimeMillis();

        long differenceMillis= now - lastSeen;

        if (differenceMillis<0){
            differenceMillis = 0;
        }

        long differenceSeconds = differenceMillis /1000;

        if(differenceSeconds< properties.getOnlineThresholdSeconds()){
            return "Online" ;
        }

        if(differenceSeconds< 3600){

            long minutes = differenceSeconds/60;

            return "Last Active: "
                    + minutes
                    + " minutes ago";
        }

        if (differenceSeconds < 86400) {

            long hours =
                    differenceSeconds / 3600;

            return "Last active: "
                    + hours
                    + " hours ago";
        }



        long days =
                differenceSeconds / 86400;

        return "Last active: "
                + days
                + " days ago";
    }



    private String buildKey(Long userId){

        return KEY_PREFIX + userId;
    }

}
