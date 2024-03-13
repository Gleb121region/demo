package com.example.demo.component;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RedisCacheManager {

    private final RedisTemplate<String, Object> redisTemplate;

    public void put(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public Optional<Object> get(String key) {
        return Optional.ofNullable(redisTemplate.opsForValue().get(key));
    }

    public void evict(String key) {
        redisTemplate.delete(key);
    }
}
