package org.example.neptuneojserver.dto.submission;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.neptuneojserver.dto.judges.JudgeStatusDTO;
import org.example.neptuneojserver.dto.judges.JudgeWebSocketDTO;
import org.example.neptuneojserver.dto.problem.TestcaseDTO;
import org.example.neptuneojserver.models.JudgeStatus;

import java.time.ZonedDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class SubmissionResponseDTO {
    private Long submissionId;
    private Long problemId;
    private String problemTitle;
    private String username;
    private String fullName;
    private String status;
    private String result;
    private Float timeRun;
    private Float memoryRun;
    private Long testAccept;
    private Long numberTest;
    private ZonedDateTime createdAt;
    private List<JudgeWebSocketDTO> judgeStatusDTOs;

    public SubmissionResponseDTO(Long submissionId, Long problemId, String problemTitle, String username, String fullName,
                                 String status, String result, Float timeRun, Float memoryRun, Long testAccept,
                                 Long numberTest, ZonedDateTime createdAt, List<JudgeStatus> judgeStatuses) {
        this.submissionId = submissionId;
        this.problemId = problemId;
        this.problemTitle = problemTitle;
        this.username = username;
        this.fullName = fullName;
        this.status = status;
        this.result = result;
        this.timeRun = timeRun;
        this.memoryRun = memoryRun;
        this.testAccept = testAccept;
        this.numberTest = numberTest;
        this.createdAt = createdAt;
        this.judgeStatusDTOs = judgeStatuses.stream().map(judgeStatus -> new JudgeWebSocketDTO(
                judgeStatus.getSubmission().getId(),
                judgeStatus.getTestcase().getIndexInProblem(),
                judgeStatus.getStatus(),
                judgeStatus.getTimeRun(),
                judgeStatus.getMemoryRun()
        )).toList();
    }
}
