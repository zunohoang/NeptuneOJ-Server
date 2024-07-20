package org.example.neptuneojserver.services;

import lombok.AllArgsConstructor;
import org.example.neptuneojserver.configs.RabbitMQConfig;
import org.example.neptuneojserver.dto.judges.JudgeStatusDTO;
import org.example.neptuneojserver.dto.submission.SubmissionDTO;
import org.example.neptuneojserver.dto.submission.SubmissionResponseDTO;
import org.example.neptuneojserver.models.*;
import org.example.neptuneojserver.repositorys.ProblemRepository;
import org.example.neptuneojserver.repositorys.SubmissionRepository;
import org.example.neptuneojserver.repositorys.UserRepository;
import org.example.neptuneojserver.websockets.SubmissionWebSocketDTO;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.ZonedDateTime;

@Service
@AllArgsConstructor
public class SubmissionService {

    private final SubmissionRepository submissionRepository;
    private final UserRepository userRepository;
    private final ProblemRepository problemRepository;
    private final RabbitTemplate rabbitTemplate;
    private SimpMessagingTemplate simpMessagingTemplate;

    public String judgeCode(SubmissionDTO submissionDTO, Long problemId, String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new IllegalArgumentException("User not found with username: " + username);
        }
        Problem problem = problemRepository.findById(problemId)
                .orElseThrow(() -> new IllegalArgumentException("Problem not found with id: " + problemId));

        Submission submission = new Submission();
        submission.setSourceCode(submissionDTO.getSourceCode());
        submission.setCreatedAt(ZonedDateTime.now());
        submission.setUser(user);
        submission.setProblem(problem);
        submission.setContest(null);
        submission.setStatus("Pending");
        submission.setResult("Waiting for judge");

        submission = submissionRepository.save(submission);

        simpMessagingTemplate.convertAndSend("/topic/submission",
                new SubmissionWebSocketDTO(submission.getId(),
                        submission.getProblem().getId(),
                        submission.getProblem().getTitle(),
                        submission.getUser().getUsername(),
                        submission.getUser().getFullName(),
                        "Pending"
                        ));

        rabbitTemplate.convertAndSend(RabbitMQConfig.JUDGE_QUEUE, String.valueOf(submission.getId()));

        return "{'submissionId': " + submission.getId() + "}";
    }

//    public List<JudgeStatusDTO> saveSubmission(SubmissionDTO submissionDTO, Long problemId, String username) {
//        User user = userRepository.findByUsername(username);
//        if (user == null) {
//            throw new IllegalArgumentException("User not found with username: " + username);
//        }
//        Problem problem = problemRepository.findById(problemId)
//                .orElseThrow(() -> new IllegalArgumentException("Problem not found with id: " + problemId));
//
//        Submission submission = new Submission();
//        submission.setSourceCode(submissionDTO.getSourceCode());
//        submission.setCreatedAt(ZonedDateTime.now());
//        submission.setUser(user);
//        submission.setProblem(problem);
//        submission.setContest(null);
//        submission.setStatus("Pending");
//        submission.setResult("Waiting for judge");
//
//        submission = submissionRepository.save(submission);
//
//        List<TestcaseDTO> testcases = problem.getTestcases().stream()
//                .map(tc -> new TestcaseDTO(tc.getId(), tc.getInput(), tc.getOutput(), tc.getIndexInProblem()))
//                .collect(Collectors.toList());
//
//        List<JudgeStatus> judgeStatuses = judgeService.judgeWithTestCase(submission.getId(), submissionDTO.getSourceCode(), testcases, problem);
//        boolean checkTLE = false;
//        boolean checkAC = false;
//        boolean checkWA = false;
//        boolean checkRE = false;
//        boolean checkCE = false;
//        for (JudgeStatus judgeStatus : judgeStatuses) {
//            if (judgeStatus.getStatus().equals("RE")) {
//                checkRE = true;
//                break;
//            }
//            if (judgeStatus.getStatus().equals("CE")) {
//                checkCE = true;
//                break;
//            }
//            if (judgeStatus.getStatus().equals("TLE")) {
//                checkTLE = true;
//                break;
//            }
//            if (judgeStatus.getStatus().equals("WA")) {
//                checkWA = true;
//                break;
//            }
//        }
//
//        if(checkTLE) submission.setResult("TLE");
//        else if(checkWA) submission.setResult("WA");
//        else if(checkRE) submission.setResult("RE");
//        else if(checkCE) submission.setResult("CE");
//        else submission.setResult("AC");
//
//        submission.setStatus("Done");
//        submission.setJudgeStatuses(judgeStatuses);;
//
//        submissionRepository.save(submission);
//
//        if(submission.getResult().equals("AC")) userService.setRank(user, submission);
//
//        return judgeStatuses.stream()
//                .map(this::convertToJudgeStatusDTO)
//                .collect(Collectors.toList());
//    }

    private JudgeStatusDTO convertToJudgeStatusDTO(JudgeStatus judgeStatus) {
        JudgeStatusDTO judgeStatusDTO = new JudgeStatusDTO();
        judgeStatusDTO.setId(judgeStatus.getIndex_in_testcase());
        judgeStatusDTO.setStatus(judgeStatus.getStatus());
        judgeStatusDTO.setSubmissionId(judgeStatus.getSubmission().getId());
        judgeStatusDTO.setTimeRun(judgeStatus.getTimeRun());
        judgeStatusDTO.setMemoryRun(judgeStatus.getMemoryRun());
        return judgeStatusDTO;
    }

    public SubmissionResponseDTO getSubmissionById(Long id, Principal principal) {
        return submissionRepository.findBySubmissionId(id);
    }

    public Page<?> getSubmissions(int page, int size) {
        Pageable pageable = PageRequest.of(Math.toIntExact(page - 1), Math.toIntExact(size), Sort.by("createdAt").descending());
        return submissionRepository.findAllPaged(pageable);
    }

    public Page<?> getSubmissionsByUserUsername(String username, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("createdAt").descending());
        return submissionRepository.findByUserUsername(username, pageable);
    }

    public Page<?> getSubmissionsByProblemId(Long problemId, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("createdAt").descending());
        return submissionRepository.findByProblemId(problemId, pageable);
    }

    public Page<?> getSubmissionsByProblemIdAndUserUsername(Long problemId, String username, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("createdAt").descending());
        return submissionRepository.findByProblemIdAndUserUsername(problemId, username, pageable);
    }
}
