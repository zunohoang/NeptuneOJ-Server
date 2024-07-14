package org.example.neptuneojserver.services;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.example.neptuneojserver.dto.problem.TestcaseDTO;
import org.example.neptuneojserver.models.Submission;
import org.example.neptuneojserver.models.Testcase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.List;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class JudgeService {

    private String projectDirectory = "E:\\1\\NeptuneOJ\\neptuneoj-server\\judge\\";

    private static final Logger logger = LoggerFactory.getLogger(DockerJudgeService.class);

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


    public String judgeWithTestCase(Long submissionId, String sourceCode, List<TestcaseDTO> testcases) {

        String judgeName = "triton";

        createFileWithSourceCode(sourceCode, "triton", submissionId);
        if(compilerCode(judgeName, submissionId).equals("Compilation error")) return "Compilation error";


        int count = 0;
        for (TestcaseDTO testcase : testcases) {
            String input = testcase.getInput();
            String expectedOutput = testcase.getOutput().trim();
            String output = runCode(judgeName, submissionId, input).trim();
            if (output.equals(expectedOutput)) count += 1;
            System.out.println("[TestCase: " + testcase.getId() + "]"
                            + "\n-Input: " + input
                            + "\n-Expected Output: " + expectedOutput
                            + "\n-Output: " + output
                            + "\n-Result: " + (output.equals(expectedOutput) ? "Accepted" : "Wrong Answer")
                    );
        }

        deleteFile(projectDirectory + File.separator + judgeName + File.separator + submissionId + ".cpp");
        deleteFile(projectDirectory + File.separator + judgeName + File.separator + submissionId + ".exe");
        return "Result: " + count + " out of " + testcases.size() + " testcases accepted.";
    }

    public String compilerCode(String judgeName, Long submissionId) {
        try {
            String[] cmd = {
                    "docker", "run", "--rm", "-i",
                    "-v", projectDirectory + judgeName + ":/home/judge",
                    judgeName, "sh", "-c", "g++ -g " + submissionId + ".cpp -o " + submissionId + ".exe"
            };

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

    public String runCode(String judgeName, Long submissionId, String input) {
        try {
            String[] cmd = new String[] {
                    "docker", "run", "--rm", "-i",
                    "-v", projectDirectory + judgeName + ":/home/judge",
                    judgeName, "sh", "-c", "./" + submissionId + ".exe"
            };

            ProcessBuilder processBuilder = new ProcessBuilder(cmd);
            Process process = processBuilder.start();

            PrintWriter writer = new PrintWriter(new OutputStreamWriter(process.getOutputStream()));
            writer.println(input);
            writer.flush();

            String output = readStream(process.getInputStream());

            int exitCode = process.waitFor();
            if (exitCode != 0) {
                logger.error("Runtime error for submission {}", submissionId);
            }

            return output;
        } catch (Exception e) {
            logger.error("Error judging submission {}: {}", submissionId, e.getMessage());
            e.printStackTrace();
        }

        return "Error";
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

}