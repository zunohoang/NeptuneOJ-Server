package org.example.neptuneojserver.caches;

import jakarta.annotation.PostConstruct;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.example.neptuneojserver.dto.judges.EngineDTO;
import org.example.neptuneojserver.models.JudgeEngine;
import org.hibernate.annotations.Cache;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.example.neptuneojserver.repositories.JudgeEngineRepository;

import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class EngineCache {

    private final String key = "judge_engines";

    private RedisTemplate<String, EngineDTO> redisTemplate;

    private final JudgeEngineRepository judgeEngineRepository;

    @PostConstruct
    public void init() {
        List<JudgeEngine> judgeEngines = judgeEngineRepository.findAll();
        List<EngineDTO> engineDTOS = new ArrayList<>();
        for (JudgeEngine judgeEngine : judgeEngines) {
            EngineDTO engine = new EngineDTO();
            engine.setName(judgeEngine.getName());
            engine.setStatus("READY");
            engineDTOS.add(engine);
            System.out.println("Engine: " + engine.getName() + " Status: " + engine.getStatus());
        }
        this.setEngines(engineDTOS);
        System.out.println("Judge Engine Service Initialized");
    }

    public void addEngine(EngineDTO engine) {
        redisTemplate.opsForList().rightPush(key, engine);
    }

    public List<EngineDTO> getAllEngines() {
        return redisTemplate.opsForList().range(key, 0, getSize());
    }

    public Long getSize() {
        return redisTemplate.opsForList().size(key);
    }

    public EngineDTO getEngine(String name) {
        List<EngineDTO> engines = getAllEngines();
        for (EngineDTO engine : engines) {
            if (engine.getName().equals(name)) {
                return engine;
            }
        }
        return null;
    }

    public void removeEngine(String name) {
        List<EngineDTO> engines = getAllEngines();
        for (EngineDTO engine : engines) {
            if (engine.getName().equals(name)) {
                redisTemplate.opsForList().remove(key, 1, engine);
            }
        }
    }

    public void removeAllEngines() {
        redisTemplate.delete(key);
    }

    public void setEngines(@NotNull List<EngineDTO> engines) {
        redisTemplate.opsForList().rightPushAll(key, engines);
    }

    public void updateEngine(String name, String status) {
        EngineDTO engine = getEngine(name);
        assert engine != null;
        engine.setStatus(status);
        removeEngine(name);
        addEngine(engine);
    }

    public EngineDTO waitingEngineStatus(String status) {
        long startTime = System.currentTimeMillis();
        long timeout = 120000;

        while (true) {
            List<EngineDTO> engines = getAllEngines();
            for (EngineDTO engine : engines) {
                System.out.println(engine.getStatus());
                if (engine.getStatus().equals(status)) {
                    return engine;
                }
            }

            if (System.currentTimeMillis() - startTime > timeout) {
                throw new RuntimeException("Timed out waiting for engine with status: " + status);
            }

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(e);
            }
        }
    }


}
