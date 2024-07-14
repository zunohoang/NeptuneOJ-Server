package org.example.neptuneojserver.dto.problem;

import lombok.Data;
import org.example.neptuneojserver.models.Tag;

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
    private List<TagDTO> tags;
    private List<TestExampleDTO> testExamples;
    private List<TestcaseDTO> testcases;
}