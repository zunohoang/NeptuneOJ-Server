package org.example.neptuneojserver.dto.judges;

import lombok.Data;

@Data
public class JudgeStatusDTO {
    private Long id;
    private String status;
    private Float timeRun;
    private Float memoryRun;
    private Long submissionId;
}
