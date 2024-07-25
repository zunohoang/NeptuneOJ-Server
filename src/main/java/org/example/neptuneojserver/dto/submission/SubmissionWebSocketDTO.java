package org.example.neptuneojserver.dto.submission;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubmissionWebSocketDTO {
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
}
