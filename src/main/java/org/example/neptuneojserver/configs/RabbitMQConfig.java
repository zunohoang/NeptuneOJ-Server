package org.example.neptuneojserver.configs;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String JUDGE_QUEUE = "judge_queue";

    @Bean
    public Queue JudgeQueue() {
        return new Queue(JUDGE_QUEUE, true);
    }
}
