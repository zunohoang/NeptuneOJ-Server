package org.example.neptuneojserver.services;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.example.neptuneojserver.models.JudgeEngine;
import org.example.neptuneojserver.models.JudgeStatus;
import org.example.neptuneojserver.models.Submission;
import org.example.neptuneojserver.models.Testcase;
import org.example.neptuneojserver.repositorys.JudgeEngineRepository;
import org.example.neptuneojserver.repositorys.SubmissionRepository;
import org.example.neptuneojserver.utils.DataStream;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.io.*;

@Service
@AllArgsConstructor
public class EngineService {

    private final JudgeEngineRepository judgeEngineRepository;
    private final RedisService redisService;
    private final JudgeService judgeService;
    private final DataStream dataStream;
    private final SubmissionRepository submissionRepository;

    @PostConstruct
    public void init() {
        List<JudgeEngine> judgeEngines = judgeEngineRepository.findAll();
        for (JudgeEngine judgeEngine : judgeEngines) {
            redisService.addElementToList("judgeEngines", judgeEngine);
        }
        System.out.println("Judge Engine Service Initialized");
    }

    @RabbitListener(queues = "judge-engine")
    public void listen(Submission submission) {
        List<Object> judgeEngines = redisService.getAllElementsFromList("judgeEngines");
        for(Object judgeEngine : judgeEngines) {
            JudgeEngine engine = (JudgeEngine) judgeEngine;
            if(engine.getStatus().equals("READY")) {
                engine.setStatus("BUSY");
                // run code
                this.runEngine(engine, submission);
                return;
            }
        }
        listen(submission);
    }

    @Async
    public void runEngine(JudgeEngine engine,Submission submission) {
        try {
            if (compilerCode(submission.getId(), submission.getSourceCode(), "cpp")) {

                List<JudgeStatus> judgeStatuses = submission.getJudgeStatuses();
                for(Testcase testcase : submission.getProblem().getTestcases()) {
                    JudgeStatus judgeStatus = runCode(
                            submission
                            , engine
                            , "cpp"
                            , testcase
                    );
                    judgeStatuses.add(judgeStatus);
                }
                submission.setJudgeStatuses(judgeStatuses);
                submissionRepository.save(submission);
                engine.setStatus("READY");
                redisService.removeElementFromList("judgeEngines", engine);
                redisService.addElementToList("judgeEngines", engine);
            } else {
                CEStatus(engine, submission);
            }
        } catch (Exception e) {
            CEStatus(engine, submission);
            return;
        }
    }

    private void CEStatus(JudgeEngine engine, Submission submission) {
        submission.setResult("CE");
        submission.setStatus("Completed");
        submissionRepository.save(submission);
        engine.setStatus("READY");
        redisService.removeElementFromList("judgeEngines", engine);
        redisService.addElementToList("judgeEngines", engine);
        throw new RuntimeException("Compile error");
    }

    public JudgeStatus runCode(Submission submission, JudgeEngine engine, String language, Testcase testcase) throws Exception {
        if(language.equals("cpp")) {
            // docker exec moon_engine sh -c "time timeout 1s /home/judge/123.exe"
            String[] cmd = {
                    "docker", "exec", engine.getName(),
                    "bash", "-c", "/home/judge/" + submission.getId() + ".exe"
            };

            ProcessBuilder processBuilder = new ProcessBuilder(cmd);
            Process process = processBuilder.start();

            PrintWriter writer = new PrintWriter(new OutputStreamWriter(process.getOutputStream()));
            writer.println(testcase.getInput());
            writer.flush();

            String output = dataStream.readStream(process.getInputStream());
            String timeOutput = dataStream.readStream(process.getErrorStream());

            int exitCode = process.waitFor();

            JudgeStatus judgeStatus = new JudgeStatus();
            judgeStatus.setInput(testcase.getInput());
            judgeStatus.setOutput(output.trim());
            judgeStatus.setSubmission(submission);
            judgeStatus.setTimeRun(dataStream.parseRealTime(timeOutput));
            judgeStatus.setMemoryRun(0F);
            judgeStatus.setExpectedOutput(testcase.getOutput());
            judgeStatus.setIndex_in_testcase(testcase.getIndexInProblem());



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

    public boolean compilerCode(Long submissionId, String sourceCode, String language) throws Exception{
        if(language.equals("cpp")) {
            String[] cmd = {
                    "docker", "exec", "", "sh", "-c", "echo '" + sourceCode + "' > /app/" + submissionId + ".cpp && g++ /app/" + submissionId + ".cpp -o /app/" + submissionId + ".exe"
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

}
