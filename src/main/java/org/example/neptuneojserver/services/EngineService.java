package org.example.neptuneojserver.services;

import lombok.AllArgsConstructor;
import org.example.neptuneojserver.caches.EngineCache;
import org.example.neptuneojserver.dto.judges.EngineDTO;
import org.example.neptuneojserver.dto.problem.TestcaseDTO;
import org.example.neptuneojserver.models.*;
import org.example.neptuneojserver.repositories.JudgeEngineRepository;
import org.example.neptuneojserver.repositories.JudgeRepository;
import org.example.neptuneojserver.repositories.ProblemRepository;
import org.example.neptuneojserver.repositories.SubmissionRepository;
import org.example.neptuneojserver.utils.DataStream;
import org.example.neptuneojserver.websockets.JudgeWebSocketDTO;
import org.example.neptuneojserver.websockets.SubmissionWebSocketDTO;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.io.*;

@Service
@AllArgsConstructor
public class EngineService {

    private final JudgeEngineRepository judgeEngineRepository;
    private final DataStream dataStream;
    private final SubmissionRepository submissionRepository;
    private final ProblemRepository problemRepository;
    private final JudgeRepository judgeRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final EngineCache engineCache;

    @Async
    public void runEngine(EngineDTO engine, Long submissionId) {
        Submission submission = submissionRepository.findById(submissionId).orElseThrow();
        Problem problem = problemRepository.getProblemById(submission.getProblem().getId());
        simpMessagingTemplate.convertAndSend("/topic/submission", new SubmissionWebSocketDTO(submission.getId(), submission.getProblem().getId(), submission.getProblem().getTitle(), submission.getUser().getUsername(), submission.getUser().getFullName(), "Running"));
        try {
            if (compilerCode(engine, submission.getId(), submission.getSourceCode(), "cpp")) {
                List<JudgeStatus> judgeStatuses = new ArrayList<>();
                List<TestcaseDTO> testcaseDTOS = problem.getTestcases().stream()
                        .map(testcase -> {
                            TestcaseDTO testcaseDTO = new TestcaseDTO();
                            testcaseDTO.setId(testcase.getId());
                            testcaseDTO.setInput(testcase.getInput());
                            testcaseDTO.setOutput(testcase.getOutput());
                            testcaseDTO.setIndexInProblem(testcase.getIndexInProblem());
                            return testcaseDTO;
                        })
                        .toList();
                boolean checkAC = false;
                boolean checkWA = false;
                boolean checkTLE = false;
                boolean checkRTE = false;
                for(TestcaseDTO testcase : testcaseDTOS) {
                    System.out.println("Running testcase " + testcase.getIndexInProblem() + " for submission " + submission.getId());
                    JudgeStatus judgeStatus = runCode(submission, engine, "cpp", testcase);
                    switch (judgeStatus.getStatus()) {
                        case "AC" -> checkAC = true;
                        case "WA" -> checkWA = true;
                        case "TLE" -> checkTLE = true;
                        default -> checkRTE = true;
                    }
                    System.out.println("Username: " + submission.getUser().getUsername());
                    simpMessagingTemplate.convertAndSendToUser(submission.getUser().getUsername(), "/topic/judge", new JudgeWebSocketDTO(submission.getId(), judgeStatus.getIndex_in_testcase(), judgeStatus.getStatus(), judgeStatus.getTimeRun(), judgeStatus.getMemoryRun()));
                    judgeStatuses.add(judgeStatus);
                }

                if(checkTLE) {
                    submission.setResult("TLE");
                } else if(checkRTE) {
                    submission.setResult("RTE");
                } else if(checkWA) {
                    submission.setResult("WA");
                } else {
                    submission.setResult("AC");
                }
                submission.setStatus("Completed");
                submission.setJudgeStatuses(judgeStatuses);
                submissionRepository.save(submission);
                simpMessagingTemplate.convertAndSend("/topic/submission", new SubmissionWebSocketDTO(submission.getId(), submission.getProblem().getId(), submission.getProblem().getTitle(), submission.getUser().getUsername(), submission.getUser().getFullName(), submission.getResult()));
                engineCache.updateEngine(engine.getName(), "READY");
            } else {
                CEStatus(engine, submission);
            }
        } catch (Exception e) {
            System.out.println("CE" + e.getMessage());
            CEStatus(engine, submission);
        }
    }

    private void CEStatus(EngineDTO engine, Submission submission) {
        submission.setResult("CE");
        submission.setStatus("Completed");
        submissionRepository.save(submission);
        simpMessagingTemplate.convertAndSend("/topic/submission",
                new SubmissionWebSocketDTO(submission.getId(),
                        submission.getProblem().getId(),
                        submission.getProblem().getTitle(),
                        submission.getUser().getUsername(),
                        submission.getUser().getFullName(),
                        "CE"
                ));
        engineCache.updateEngine(engine.getName(), "READY");
        throw new RuntimeException("Compile error");
    }

    public JudgeStatus runCode(Submission submission, EngineDTO engine, String language, TestcaseDTO testcase) throws Exception {
        if(language.equals("cpp")) {
            // docker exec moon_engine sh -c "time timeout 1s /home/judge/123.exe"
            String[] cmd = {
                    "docker", "exec", "-i", engine.getName(),
                    "bash", "-c", "time timeout " + submission.getProblem().getTimeLimit() + "s /home/judge/" + submission.getId() + ".exe"
            };

            ProcessBuilder processBuilder = new ProcessBuilder(cmd);
            Process process = processBuilder.start();

            PrintWriter writer = new PrintWriter(new OutputStreamWriter(process.getOutputStream()));
            writer.println(testcase.getInput());
            writer.flush();
            System.out.println(testcase.getInput());

            String output = readStream(process.getInputStream());
            String timeOutput = readStream(process.getErrorStream());

            System.out.println("Output: " + output);
            System.out.println("Time: " + timeOutput);

            int exitCode = process.waitFor();

            JudgeStatus judgeStatus = new JudgeStatus();
            judgeStatus.setInput(testcase.getInput());
            judgeStatus.setOutput(output.trim());
            judgeStatus.setSubmission(new Submission(submission.getId()));
            judgeStatus.setTimeRun(parseRealTime(timeOutput));
            judgeStatus.setMemoryRun(0F);
            judgeStatus.setExpectedOutput(testcase.getOutput());
            judgeStatus.setIndex_in_testcase(testcase.getIndexInProblem());
            judgeStatus.setTestcase(new Testcase(testcase.getId()));


            if (exitCode == 124) {
                // Timeout exit code
                judgeStatus.setStatus("TLE");
                judgeStatus.setOutput("TLE");
                return judgeStatus;
            } else if (exitCode != 0) {
                System.out.println("RTE for submission " + submission.getId());
            }

            judgeStatus.setStatus(output.trim().equals(testcase.getOutput().trim()) ? "AC" : "WA");

            return judgeStatus;
        } else {
            throw new RuntimeException("Language not supported");
        }
    }

    public boolean compilerCode(EngineDTO engine, Long submissionId, String sourceCode, String language) throws Exception{
        if(language.equals("cpp")) {
            String[] cmd = {
                    "docker", "exec", engine.getName(), "sh", "-c", "echo '" + sourceCode + "' > /home/judge/" + submissionId + ".cpp && g++ /home/judge/" + submissionId + ".cpp -o /home/judge/" + submissionId + ".exe"
            };

            ProcessBuilder processBuilder = new ProcessBuilder(cmd);
            Process process = processBuilder.start();
            int exitCode = process.waitFor();

            String output = dataStream.readStream(process.getInputStream());
            String error = dataStream.readStream(process.getErrorStream());

            System.out.println("O: " + output);
            System.out.println("E: " + error);
            System.out.println("I: " + exitCode);

            return exitCode == 0;
        } else {
            return false;
        }
    }

    public void runContainer(String containerName) throws Exception {
        String[] cmd = {
                "docker", "start", containerName
        };

        ProcessBuilder processBuilder = new ProcessBuilder(cmd);
        Process process = processBuilder.start();
        process.waitFor();
    }

    public void stopContrainer(String containerName) throws Exception{
        String[] cmd = {
                "docker", "stop", containerName
        };

        ProcessBuilder processBuilder = new ProcessBuilder(cmd);
        Process process = processBuilder.start();
        process.waitFor();
    }

    private String readStream(java.io.InputStream stream) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        StringBuilder output = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            output.append(line).append("\n");
        }
        return output.toString();
    }

    private static float parseRealTime(String timeOutput) {
        String[] lines = timeOutput.split("\n");
        for (String line : lines) {
            if (line.startsWith("real")) {
                // Tách lấy giá trị 'real' (ví dụ: real    0m0.004s)
                String[] parts = line.split("\\s+");
                if (parts.length >= 2) {
                    String timeStr = parts[1].trim(); // Lấy phần tử thứ hai và loại bỏ khoảng trắng
                    return convertTimeToSeconds(timeStr); // Chuyển đổi thành giây và trả về
                }
            }
        }
        return -1; // Trường hợp không tìm thấy giá trị 'real'
    }

    private static float convertTimeToSeconds(String timeStr) {
        try {
            // Kiểm tra đơn vị thời gian (m, s) và tính toán
            float seconds = 0;
            if (timeStr.contains("m")) {
                String[] parts = timeStr.split("m");
                float minutes = Float.parseFloat(parts[0]);
                float secondsPart = Float.parseFloat(parts[1].replace("s", ""));
                seconds = minutes * 60 + secondsPart;
            } else if (timeStr.contains("s")) {
                seconds = Float.parseFloat(timeStr.replace("s", ""));
            }
            return seconds;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return -1; // Xảy ra lỗi chuyển đổi
        }
    }
}
