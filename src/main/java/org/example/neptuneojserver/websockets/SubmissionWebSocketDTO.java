package org.example.neptuneojserver.websockets;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SubmissionWebSocketDTO {
    private Long submissionId;
    private Long problemId;
    private String problemTitle;
    private String username;
    private String fullName;
    private String status;
}
