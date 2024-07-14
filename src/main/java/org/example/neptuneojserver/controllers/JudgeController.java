package org.example.neptuneojserver.controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.example.neptuneojserver.services.JudgeService;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1")
@SecurityRequirement(name = "Bearer Authentication")
public class JudgeController {

    private final JudgeService judgeService;



}
