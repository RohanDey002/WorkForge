package com.taskmanagement.aitaskmanagement.Redis.config;

import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.JavaType;
import tools.jackson.databind.ObjectMapper;

@RequiredArgsConstructor
public class TypeJsonRedisSerializer<T> implements RedisSerializer<T> {

    private final ObjectMapper objectMapper;
    private final JavaType javaType;

    @Override
    public byte[] serialize( T value) throws SerializationException {

        if (value==null) return new byte[0];

        try {

            return objectMapper.writeValueAsBytes(value);
        } catch (JacksonException exception) {

            throw new SerializationException("Failed to serialize Redis cache value",exception);

        }
    }

    @Override
    public  T deserialize(byte  [] bytes) throws SerializationException {

        if(bytes==null || bytes.length==0) return null;

        try {

            return objectMapper.readValue(bytes,javaType);
        } catch (JacksonException exception) {

            throw  new SerializationException("Failed deserialize Redis cache value", exception);
        }
    }
}
