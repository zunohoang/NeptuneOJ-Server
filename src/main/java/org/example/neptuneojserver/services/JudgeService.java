package org.example.neptuneojserver.services;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class JudgeService {

    public void createFileWithSourceCode(String sourceCode, Long id) {
        String fileName = "source_code_" + id + ".c";
        File file = new File(fileName);

    }

}