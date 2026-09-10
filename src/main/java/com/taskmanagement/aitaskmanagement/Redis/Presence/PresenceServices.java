package com.taskmanagement.aitaskmanagement.Redis.Presence;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PresenceServices {
    private static final String KEY_PREFIX ="presence:user:";

    private final StringRedisTemplate redisTemplate;
    private final PresenceProperties properties;


    public void  recordLastActive(Long userId){

        if(userId==null) return;

        String key =  buildKey(userId);

        Long now = System.currentTimeMillis();

        String existingValue = redisTemplate.opsForValue().get(key);

        if(existingValue!=null){

            try {

                long lastSeen = Long.parseLong(existingValue);

                long difference = now - lastSeen;

                long updateIntervalsMillis = properties.getUpdateIntervalsSeconds()*1000L;

                if(difference<updateIntervalsMillis){
                    return;
                }

            } catch (NumberFormatException e) {

                return;
            }

        }

        redisTemplate.opsForValue().set(
                key,
                String.valueOf(now),
                Duration.ofDays(properties.getTtlDays())
        );

    }

    public Long getLastSeen(Long userId){

        if (userId==null) return null;

        String key = buildKey(userId);

        String value = redisTemplate.opsForValue().get(key);

        if(value== null) return null;

        try {

            return Long.parseLong(value);

        } catch (NumberFormatException e) {

            return null;
        }
    }

    public  String getPresence(Long userId){

        Long lastSeen = getLastSeen(userId);

        return getLastActive(lastSeen);
    }


    public String getLastActive(Long lastSeen){


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

    public Map<Long,String> getPresenceForUsers(List<Long> userIds){

        if(userIds ==null || userIds.isEmpty()){
            return Collections.emptyMap();
        }
        List<String> keys = userIds.stream()
                .map(this::buildKey)
                .toList();

        List<String> values = redisTemplate.opsForValue()
                .multiGet(keys);

        Map<Long,String> presenceMap = new HashMap<>();

        if (values==null){
            for (Long userId : userIds){
                presenceMap.put(userId,
                        "Last Active: Unknown");
            }

            return presenceMap;
        }

        for (int i = 0; i < userIds.size(); i++) {

            Long userId = userIds.get(i);

            String value = i<values.size()?
                    values.get(i) : null;


            Long lastSeen = null;

            if(value!=null){
                try {
                    lastSeen = Long.parseLong(value);
                } catch (NumberFormatException e) {

                }
            }

            presenceMap.put(userId,getLastActive(lastSeen));
        }

          return presenceMap;

    }



    private String buildKey(Long userId){

        return KEY_PREFIX + userId;
    }

}
