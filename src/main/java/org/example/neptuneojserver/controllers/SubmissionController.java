package org.example.neptuneojserver.controllers;


import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.example.neptuneojserver.dto.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1")
@SecurityRequirement(name = "Bearer Authentication")
public class SubmissionController {

}
