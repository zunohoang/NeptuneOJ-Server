package org.example.neptuneojserver.dto.problem;

import lombok.Data;

import java.time.ZonedDateTime;
import java.util.List;

@Data
public class ProblemRequestDTO {
    private String title;
    private String body;
    private Float point;
    private Long numberAccept;
    private Float timeLimit;
    private Long memory;
    private ZonedDateTime createdAt;
    private boolean hidden;
    private Long authorId;
    private List<TestExampleDTO> testExamples;
    private List<TestcaseDTO> testcases;
}