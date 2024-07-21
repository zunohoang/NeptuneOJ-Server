package org.example.neptuneojserver.listeners;

import org.example.neptuneojserver.caches.EngineCache;
import org.example.neptuneojserver.configs.RabbitMQConfig;
import org.example.neptuneojserver.dto.judges.EngineDTO;
import org.example.neptuneojserver.services.EngineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

@Component
public class RabbitMQListener {

    @Autowired
    private EngineService engineService;

    @Autowired
    private EngineCache engineCache;

    @RabbitListener(queues = RabbitMQConfig.JUDGE_QUEUE)
    public void listen(String s) {
        EngineDTO engineDTO = engineCache.waitingEngineStatus("READY");
        engineCache.updateEngine(engineDTO.getName(), "BUSY");
        System.out.println("Received: " + s);
        engineService.runEngine(engineDTO, Long.parseLong(s)); // Gọi phương thức bất đồng bộ
        System.out.println("Completed: " + s); // Sẽ được in ra ngay lập tức sau khi gọi runEngine
    }
}
