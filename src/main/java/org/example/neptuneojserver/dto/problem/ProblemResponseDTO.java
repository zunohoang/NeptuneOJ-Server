package org.example.neptuneojserver.dto.problem;

import lombok.Data;
import org.example.neptuneojserver.models.TestExample;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class ProblemResponseDTO {
    private Long id;
    private String title;
    private String body;
    private float point;
    private Long numberAccept;
    private float timeLimit;
    private Long memory;
    private String authorName, authorUsername;
    private Long authorId;
    private List<TestExampleDTO> testExamples;
    private List<TestcaseDTO> testcases = new ArrayList<>();
    private ZonedDateTime createdAt;
}
