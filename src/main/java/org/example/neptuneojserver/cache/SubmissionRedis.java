package org.example.neptuneojserver.cache;

import org.example.neptuneojserver.models.Submission;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SubmissionRedis {
    private final RedisTemplate<String, List<Submission>> redisTemplate;

    public SubmissionRedis(RedisTemplate<String, List<Submission>> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void addElementToList(String key, List<Submission> element) {
        redisTemplate.opsForList().rightPush(key, element);
    }

    public Long getListSize(String key) {
        return redisTemplate.opsForList().size(key);
    }

    public List<Submission> getElementFromList(String key) {
        return redisTemplate.opsForList().leftPop(key);
    }

    public List<List<Submission>> getAllElementsFromList(String key) {
        return redisTemplate.opsForList().range(key, 0, redisTemplate.opsForList().size(key));
    }

    public void removeElementFromList(String key, List<Submission> element) {
        redisTemplate.opsForList().remove(key, 1, element);
    }
}
