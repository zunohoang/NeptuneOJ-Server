package org.example.neptuneojserver.controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import org.example.neptuneojserver.dto.Response;
import org.example.neptuneojserver.dto.submission.SubmissionDTO;
import org.example.neptuneojserver.models.Submission;
import org.example.neptuneojserver.services.JudgeService;
import org.example.neptuneojserver.services.SubmissionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1")
@SecurityRequirement(name = "Bearer Authentication")
public class SubmissionController {
    private final JudgeService judgeService;
    private final SubmissionService submissionService;

    @PostMapping("/problem/{problemId}/submission")
    public ResponseEntity<Response<?>> submitCode(@RequestBody SubmissionDTO submissionDTO, @PathVariable Long problemId, Principal principal) {
        return ResponseEntity.ok(new Response<>("success", "Nop bai thanh cong", submissionService.saveSubmission(submissionDTO, problemId, principal.getName())));
    }

    @GetMapping("/submission/{id}")
    public ResponseEntity<Response<?>> getSubmissionById(@PathVariable Long id, Principal principal) {
        return ResponseEntity.ok(new Response<>("success", "Lay ket qua nop bai thanh cong", submissionService.getSubmissionById(id, principal)));
    }

    @GetMapping("/submissions")
    public ResponseEntity<Response<?>> getSubmissions(@RequestParam int page, @RequestParam int size) {
        return ResponseEntity.ok(new Response<>("success", "Lay danh sach nop bai thanh cong", submissionService.getSubmissions(page, size)));
    }

    @GetMapping("/problem/{problemId}/submissions")
    public ResponseEntity<Response<?>> getSubmissionsByProblemId(@PathVariable Long problemId, @RequestParam int page, @RequestParam int size) {
        return ResponseEntity.ok(new Response<>("success", "Lay danh sach nop bai thanh cong", submissionService.getSubmissionsByProblemId(problemId, page, size)));
    }

    @GetMapping("/user/{username}/submissions")
    public ResponseEntity<Response<?>> getSubmissionsByUsername(@PathVariable String username, @RequestParam int page, @RequestParam int size) {
        return ResponseEntity.ok(new Response<>("success", "Lay danh sach nop bai thanh cong", submissionService.getSubmissionsByUserUsername(username, page, size)));
    }

    @GetMapping("/problem/{problemId}//submissions")
    public ResponseEntity<Response<?>> getSubmissionsByProblemIdAndUsername(@PathVariable Long problemId, @RequestParam String username, @RequestParam int page, @RequestParam int size) {
        return ResponseEntity.ok(new Response<>("success", "Lay danh sach nop bai thanh cong", submissionService.getSubmissionsByProblemIdAndUserUsername(problemId, username, page, size)));
    }
}
