package org.example.neptuneojserver.cache;

import org.example.neptuneojserver.models.JudgeEngine;
import org.hibernate.annotations.Cache;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Component
public class EngineRedis {
    private final RedisTemplate<String, List<JudgeEngine>> redisTemplate;

    public EngineRedis(RedisTemplate<String, List<JudgeEngine>> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void addElementToList(String key, List<JudgeEngine> element) {
        redisTemplate.opsForList().rightPush(key, element);
    }

    public Long getListSize(String key) {
        return redisTemplate.opsForList().size(key);
    }

    public List<JudgeEngine> getElementFromList(String key) {
        return redisTemplate.opsForList().leftPop(key);
    }

    public List<List<JudgeEngine>> getAllElementsFromList(String key) {
        return redisTemplate.opsForList().range(key, 0, redisTemplate.opsForList().size(key));
    }

    public void removeElementFromList(String key, List<JudgeEngine> element) {
        redisTemplate.opsForList().remove(key, 1, element);
    }
}
