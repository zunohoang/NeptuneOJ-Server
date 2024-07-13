package org.example.neptuneojserver.services;

import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

@Service
public class DockerJudgeService {

    private static final Logger logger = LoggerFactory.getLogger(DockerJudgeService.class);


    public static void main(String[] args) {
        (new DockerJudgeService()).judgeSubmission("1_1_test.cpp", "Hss dsvds\nsafsa");
    }

    public void judgeSubmission(String fileName, String input) {
        try {
            String[] cmd = {
                    "docker", "run", "-i",
                    "-v", "E:\\1\\NeptuneOJ\\neptuneoj-server\\src\\main\\java\\org\\example\\neptuneojserver\\archive\\cpp\\1_1_test:/home/judge",
                    "kerberos", "sh", "-c", "g++ -g 1_1_test.cpp -o 1_1_test.exe"
            };

            ProcessBuilder processBuilder = new ProcessBuilder(cmd);
            Process process = processBuilder.start();

            int exitCode = process.waitFor();
            if (exitCode != 0) {
                logger.error("Compilation error for submission {}", fileName);
                return;
            }

            cmd = new String[] {
                    "docker", "run", "-i",
                    "-v", "E:\\1\\NeptuneOJ\\neptuneoj-server\\src\\main\\java\\org\\example\\neptuneojserver\\archive\\cpp\\1_1_test:/home/judge",
                    "kerberos", "sh", "-c", "./1_1_test.exe"
            };

            processBuilder = new ProcessBuilder(cmd);
            process = processBuilder.start();

            PrintWriter writer = new PrintWriter(new OutputStreamWriter(process.getOutputStream()));
            writer.println(input);
            writer.flush();

            String output = readStream(process.getInputStream());
            System.out.println(output);

            exitCode = process.waitFor();
            if (exitCode != 0) {
                logger.error("Runtime error for submission {}", fileName);
            }

        } catch (Exception e) {
            logger.error("Error judging submission {}: {}", fileName, e.getMessage());
            e.printStackTrace();
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
