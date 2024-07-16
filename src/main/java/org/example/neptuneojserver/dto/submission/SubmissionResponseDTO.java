package org.example.neptuneojserver.dto.submission;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
public class SubmissionResponseDTO {
    private Long id;
    private String problemTitle;
    private String userFullName;
    private String result;
    private String status;
    private ZonedDateTime createdAt;
    public SubmissionResponseDTO(Long id, String problemTitle, String userFullName, String result, String status, ZonedDateTime createdAt) {
        this.id = id;
        this.problemTitle = problemTitle;
        this.userFullName = userFullName;
        this.result = result;
        this.status = status;
        this.createdAt = createdAt;
    }

    // Getters and setters
}