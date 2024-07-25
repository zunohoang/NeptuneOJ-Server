package org.example.neptuneojserver.services;

import lombok.AllArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class WebSocketService {

    private SimpMessagingTemplate simpMessagingTemplate;

    public void sendMessage(String username, String message) {
        simpMessagingTemplate.convertAndSendToUser(username, "/topic/message", message);
    }


}
