package org.example.neptuneojserver.services;

import lombok.AllArgsConstructor;
import org.example.neptuneojserver.models.Submission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class RedisService {
    private RedisTemplate<String, Object> redisTemplate;

    public void addElementToList(String key, Object value) {
        try {
            redisTemplate.opsForList().rightPush(key, value);
        } catch (Exception e) {
            throw new RuntimeException("Failed to add element to Redis list", e);
        }
    }

    public Long getListSize(String key) {
        ListOperations<String, Object> listOps = redisTemplate.opsForList();
        return listOps.size(key);
    }

    public Object getElementFromList(String key) {
        ListOperations<String, Object> listOps = redisTemplate.opsForList();
        return listOps.leftPop(key);
    }

    public List<Object> getAllElementsFromList(String key) {
        ListOperations<String, Object> listOps = redisTemplate.opsForList();
        return listOps.range(key, 0, listOps.size(key));
    }

    public void removeElementFromList(String key, Object element) {
        ListOperations<String, Object> listOps = redisTemplate.opsForList();
        listOps.remove(key, 1, element);
    }


}
