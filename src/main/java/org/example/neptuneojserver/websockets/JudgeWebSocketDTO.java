package org.example.neptuneojserver.websockets;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data@AllArgsConstructor
public class JudgeWebSocketDTO {
    private Long submissionId;
    private Long indexTestInProblem;
    private String status;
    private Float timeRun;
    private Float memoryUsed;
}
