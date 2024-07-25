package org.example.neptuneojserver.dto.judges;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.example.neptuneojserver.models.JudgeStatus;

import java.util.List;

@Data@AllArgsConstructor
public class JudgeWebSocketDTO {
    private Long submissionId;
    private Long indexTestInProblem;
    private String status;
    private Float timeRun;
    private Float memoryUsed;
}
