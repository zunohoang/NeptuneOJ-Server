package org.example.neptuneojserver.dto.judges;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JudgeStatusDTO {
    private Long id;
    private String status;
    private Float timeRun;
    private Float memoryRun;
    private Long submissionId;
}
