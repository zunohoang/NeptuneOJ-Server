package org.example.neptuneojserver.services;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.example.neptuneojserver.dto.judges.JudgeStatusDTO;
import org.example.neptuneojserver.dto.problem.TestcaseDTO;
import org.example.neptuneojserver.models.JudgeStatus;
import org.example.neptuneojserver.models.Problem;
import org.example.neptuneojserver.models.Submission;
import org.example.neptuneojserver.models.Testcase;
import org.example.neptuneojserver.repositorys.JudgeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class JudgeService {

    private final JudgeRepository judgeRepository;

    private final String projectDirectory = "E:\\1\\NeptuneOJ\\neptuneoj-server\\judge\\";

    private static final Logger logger = LoggerFactory.getLogger(DockerJudgeService.class);

    public JudgeService(JudgeRepository judgeRepository) {
        this.judgeRepository = judgeRepository;
    }


    public void createFileWithSourceCode(String sourceCode, String judgeName,Long id) {
        System.out.println(judgeName);
        String filePath = projectDirectory + File.separator + judgeName + File.separator + id + ".cpp";
        System.out.println(filePath);
        File file = new File(filePath);
        System.out.println(file.getAbsolutePath());
        try {
            PrintWriter writer = new PrintWriter(file);
            writer.println(sourceCode);
            writer.close();
        } catch (FileNotFoundException e) {
            logger.error("Error creating file {}", filePath);
        }
    }

    public List<JudgeStatus> judgeWithTestCase(Long submissionId, String sourceCode, List<TestcaseDTO> testcases, Problem problem) {

        String judgeName = "triton";

        List<JudgeStatus> judgeStatuses = new ArrayList<>();

        createFileWithSourceCode(sourceCode, "triton", submissionId);

        if(!compilerCode(judgeName, submissionId).equals("Compilation error")) {
            int count = 0;
            for (TestcaseDTO testcase : testcases) {
                judgeStatuses.add(runCode(judgeName, submissionId, problem.getTimeLimit(), problem.getMemory(), testcase));
            }
        } else {
            for(TestcaseDTO testcase : testcases) {
                JudgeStatus judgeStatus = getJudgeStatus(submissionId, testcase);
                judgeStatuses.add(judgeStatus);
            }
        }

        deleteFile(projectDirectory + File.separator + judgeName + File.separator + submissionId + ".cpp");
        deleteFile(projectDirectory + File.separator + judgeName + File.separator + submissionId + ".exe");
        return judgeStatuses;
    }

    private JudgeStatus getJudgeStatus(Long submissionId, TestcaseDTO testcase) {
        JudgeStatus judgeStatus = new JudgeStatus();
        judgeStatus.setStatus("Compilation error");
        judgeStatus.setInput(testcase.getInput());
        judgeStatus.setOutput("Compilation error");
        judgeStatus.setExpectedOutput(testcase.getOutput());
        judgeStatus.setIndex_in_testcase((testcase.getIndexInProblem()));
        judgeStatus.setSubmission(new Submission(submissionId));
        judgeStatus.setTestcase(new Testcase(testcase.getId()));
        return judgeStatus;
    }

    public String compilerCode(String judgeName, Long submissionId) {
        try {
            String[] cmd = {
                    "docker", "run", "--rm", "-i",
                    "-v", projectDirectory + judgeName + ":/home/judge",
                    judgeName, "sh", "-c", "g++ -g " + submissionId + ".cpp -o " + submissionId + ".exe"
            };

            /*
            docker run --rm -i -v E:\1\NeptuneOJ\neptuneoj-server\judge\triton:/home/judge triton sh -c "g++ -g 1.cpp -o 1.exe"
             */

            ProcessBuilder processBuilder = new ProcessBuilder(cmd);
            Process process = processBuilder.start();

            int exitCode = process.waitFor();
            if (exitCode != 0) {
                logger.error("Compilation error for submission {}", submissionId);
                return "Compilation error";
            }

            return "Compiled successfully";
        } catch (Exception e) {
            logger.error("Error judging submission {}: {}", submissionId, e.getMessage());
            e.printStackTrace();
        }

        return "Error";
    }

    public JudgeStatus runCode(String judgeName, Long submissionId, Float timeLimit, Long memoryLimit, TestcaseDTO testcase) {
        try {
            String[] cmd = new String[] {
                    "docker", "run", "--rm", "-i",
                    "--cpus=1.0", "--memory=" + memoryLimit + "m",
                    "-v", projectDirectory + judgeName + ":/home/judge",
                    judgeName, "bash", "-c", "time timeout 1s ./" + submissionId + ".exe"
            };

            /*
            docker run --rm -i --cpus=1.0 --memory=256m -v E:\1\NeptuneOJ\neptuneoj-server\judge\triton:/home/judge triton bash -c "time timeout 1s ./1.exe"
             */

            ProcessBuilder processBuilder = new ProcessBuilder(cmd);
            Process process = processBuilder.start();

            PrintWriter writer = new PrintWriter(new OutputStreamWriter(process.getOutputStream()));
            writer.println(testcase.getInput());
            writer.flush();

            String output = readStream(process.getInputStream());
            String timeOutput = readStream(process.getErrorStream());

            int exitCode = process.waitFor();

            JudgeStatus judgeStatus = new JudgeStatus();
            judgeStatus.setInput(testcase.getInput());
            judgeStatus.setOutput(output.trim());
            judgeStatus.setExpectedOutput(testcase.getOutput());
            judgeStatus.setIndex_in_testcase((testcase.getIndexInProblem()));
            judgeStatus.setSubmission(new Submission(submissionId));
            judgeStatus.setTestcase(new Testcase(testcase.getId()));
            judgeStatus.setTimeRun(parseRealTime(timeOutput));
            judgeStatus.setMemoryRun(0F);


            if (exitCode == 124) {
                // Timeout exit code
                judgeStatus.setStatus("Time limit exceeded");
                judgeStatus.setOutput("Time limit exceeded");
                return judgeStatus;
            } else if (exitCode != 0) {
                logger.error("Runtime error for submission {}", submissionId);
            }

            judgeStatus.setStatus((output.trim().equals(testcase.getOutput().trim())) ? "Accepted" : "Wrong answer");

            return judgeStatus;
        } catch (Exception e) {
            logger.error("Error judging submission {}: {}", submissionId, e.getMessage());
            e.printStackTrace();
        }

        return null;
    }


    public void deleteFile(String fileUrl) {
        File file = new File(fileUrl);
        try {
            if (file.delete()) {
                logger.info("File deleted successfully");
            } else {
                logger.error("Failed to delete the file");
            }
        } catch (Exception e) {
            logger.error("Error deleting file: {}", e.getMessage());
        }
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