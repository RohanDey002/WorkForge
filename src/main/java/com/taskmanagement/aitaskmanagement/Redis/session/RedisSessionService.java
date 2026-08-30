package com.taskmanagement.aitaskmanagement.Redis.session;

import com.taskmanagement.aitaskmanagement.Redis.RedisKeyConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

import java.time.Duration;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RedisSessionService {

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    public void createSession(
            String sessionId,
            RedisSession session,
            Duration expiration){
        try {
            String key = RedisKeyConstants.SESSION_PREFIX+sessionId;

            String value = objectMapper.writeValueAsString(session);

            redisTemplate.opsForValue()
                    .set(key,value,expiration);



        } catch (Exception exception) {
            throw new RuntimeException("Failed to serialize redis session",exception);

        }
    }

    public Optional<RedisSession> getSession(String sessionId){

        String key = RedisKeyConstants.SESSION_PREFIX+sessionId;

        String value = redisTemplate.opsForValue()
                .get(key);

        if(value==null) return Optional.empty();
        try {
            RedisSession redisSession =objectMapper.readValue(
                    value,
                    RedisSession.class
            );

            return Optional.of(redisSession);
        }catch (JacksonException exception){
            throw new RuntimeException("Failed to deserialize redis ",exception);
        }

    }

    public void updateSession(
          String sessionId,
          RedisSession redisSession,
          Duration expiration){

        createSession(sessionId, redisSession, expiration);

    }

    public void revokeSession(String sessionId){

        Optional<RedisSession> sessionOptional = getSession(sessionId);

        if(sessionOptional.isEmpty()) return;

        RedisSession session = sessionOptional.get();
        session.setRevoked(true);

        Long ttl = redisTemplate.getExpire(RedisKeyConstants.SESSION_PREFIX+sessionId);

        if(ttl==null || ttl<0) return;

        updateSession(sessionId,session,Duration.ofSeconds(ttl));
    }

    public void deleteSession(String sessionId){

        String key = RedisKeyConstants.SESSION_PREFIX+sessionId;

        redisTemplate.delete(key);
    }

    public boolean exitsSession(String sessionId){

        String key = RedisKeyConstants.SESSION_PREFIX+sessionId;

       Boolean exits = redisTemplate.hasKey(key);

        return Boolean.TRUE.equals(exits);
    }

}
