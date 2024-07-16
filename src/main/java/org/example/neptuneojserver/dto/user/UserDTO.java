package org.example.neptuneojserver.dto.user;

import lombok.Data;
import org.example.neptuneojserver.dto.problem.ProblemResponseDTO;
import org.example.neptuneojserver.dto.submission.SubmissionResponseDTO;
import org.example.neptuneojserver.models.Problem;
import org.example.neptuneojserver.models.Submission;

import java.util.List;

@Data
public class UserDTO {
    private Long id;
    private String username;
    private String fullName;
    private Float point;
    private String description;
    private Long numberOfProblems;
    private Long rank;
    private List<Problem> solvedProblems;
    private List<Problem> createdProblems;
    private List<Submission> submissions;
}
