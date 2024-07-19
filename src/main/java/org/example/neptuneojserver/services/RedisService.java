package org.example.neptuneojserver.services;

import org.example.neptuneojserver.models.Submission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RedisService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public void addElementToList(String key,Object element) {
        ListOperations<String, Object> listOps = redisTemplate.opsForList();
        listOps.rightPush(key, element);
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
