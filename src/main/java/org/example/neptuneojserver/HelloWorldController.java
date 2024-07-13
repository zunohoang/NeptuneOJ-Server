package org.example.neptuneojserver;

import org.example.neptuneojserver.services.DockerJudgeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class HelloWorldController {

    @Autowired
    private DockerJudgeService dockerJudgeService;

    @GetMapping("/helloworld")
    public String helloworld() {
        return "helloworld";
    }

    @GetMapping("/submit")
    public String submit() {

        dockerJudgeService.judgeSubmission("1_1_test.cpp", "Hss\n");

        return "submit";
    }
}
