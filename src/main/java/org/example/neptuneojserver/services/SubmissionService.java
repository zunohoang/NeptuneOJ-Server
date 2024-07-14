package org.example.neptuneojserver.services;

import lombok.AllArgsConstructor;
import org.example.neptuneojserver.dto.problem.TestcaseDTO;
import org.example.neptuneojserver.dto.submission.SubmissionDTO;
import org.example.neptuneojserver.models.Problem;
import org.example.neptuneojserver.models.Submission;
import org.example.neptuneojserver.models.Testcase;
import org.example.neptuneojserver.models.User;
import org.example.neptuneojserver.repositorys.ProblemRepository;
import org.example.neptuneojserver.repositorys.SubmissionRepository;
import org.example.neptuneojserver.repositorys.UserRepository;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class SubmissionService {

    private final SubmissionRepository submissionRepository;
    private final UserRepository userRepository;
    private final ProblemRepository problemRepository;
    private final JudgeService judgeService;

    public String saveSubmission(SubmissionDTO submissionDTO, Long problemId, String username) {
        User user = userRepository.findByUsername(username);
        Optional<Problem> optinalProblem = problemRepository.findById(problemId);

        if (user == null || optinalProblem.isEmpty()) {
            // Handle case where user is not found
            throw new IllegalArgumentException("User not found with username: " + username);
        }

        Problem problem = optinalProblem.get();

        Submission submission = new Submission();
        submission.setSourceCode(submissionDTO.getSourceCode());
        submission.setCreatedAt(ZonedDateTime.now());
        submission.setUser(user);
        submission.setProblem(problem);
        submission.setContest(null);
        submission.setStatus("Pending");
        submission.setResult("Waiting for judge");

        submission = submissionRepository.save(submission);

        List<TestcaseDTO> testcases = new ArrayList<>();
        for (Testcase testcase : problem.getTestcases()) {
            testcases.add(new TestcaseDTO(testcase.getId(), testcase.getInput(), testcase.getOutput(), testcase.getIndexInProblem()));
        }

        String result = judgeService.judgeWithTestCase(submission.getId(), submissionDTO.getSourceCode(), testcases);

        submission.setResult(result);
        submission.setStatus("Completed");

        submissionRepository.save(submission);

        return result;
    }

}
