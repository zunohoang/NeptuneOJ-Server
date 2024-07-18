package org.example.neptuneojserver.controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import org.example.neptuneojserver.dto.problem.ProblemRequestDTO;
import org.example.neptuneojserver.dto.problem.ProblemResponseDTO;
import org.example.neptuneojserver.dto.Response;
import org.example.neptuneojserver.services.ProblemService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;

@RestController
@AllArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
@RequestMapping("/api/v1")
public class ProblemController {

    private final ProblemService problemService;

    // Get problem by id
    @GetMapping("/problem/{id}")
    public ResponseEntity<Response<ProblemResponseDTO>> getProblemById(@PathVariable Long id, Principal principal) {
        if (hasAnyAuthority("ADMIN", "CREATOR")) {
            return ResponseEntity.ok(new Response<>("success", "Lấy bài tập thành công", problemService.getAdminProblemById(id)));
        } else {
            return ResponseEntity.ok(new Response<>("success", "Lấy bài tập thành công", problemService.getProblemById(id)));
        }
    }

    private boolean hasAnyAuthority(String... authorities) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return Arrays.stream(authorities).anyMatch(auth -> authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals(auth)));
    }

    // Get all problems
    @GetMapping("/problems")
    public ResponseEntity<Response<List<ProblemResponseDTO>>> getAllProblems(
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String tagName) {

        if(search != null && !search.isEmpty()) {
            return ResponseEntity.ok(new Response<List<ProblemResponseDTO>>("success", "Lay danh sach bai tap thanh cong", problemService.getProblemsBySearch(page, size, search)));
        } else if(tagName != null && !tagName.isEmpty()) {
            return ResponseEntity.ok(new Response<List<ProblemResponseDTO>>("success", "Lay danh sach bai tap thanh cong", problemService.getProblemsByTagName(page, size, tagName)));
        }

        return ResponseEntity.ok(new Response<List<ProblemResponseDTO>>("success", "Lay danh sach bai tap thanh cong", problemService.getProblems(page, size)));
    }

    // Add problem
    @PreAuthorize("hasAnyAuthority('ADMIN', 'CREATOR')")
    @PostMapping("/problem")
    public ResponseEntity<Response<ProblemRequestDTO>> addProblem(@RequestBody ProblemRequestDTO newProblem, Principal principal) {
        problemService.addProblem(newProblem, principal.getName());
        return ResponseEntity.ok(new Response<ProblemRequestDTO>("success", "Them bai tap thanh cong", newProblem));
    }

    // Update problem
    @PreAuthorize("hasAnyAuthority('ADMIN', 'CREATOR')")
    @PutMapping("/problem/{id}")
    public ResponseEntity<Response<ProblemRequestDTO>> updateProblem(@RequestBody ProblemRequestDTO problem, @PathVariable Long id) {
        problemService.updateProblem(problem, id);
        return ResponseEntity.ok(new Response<ProblemRequestDTO>("success", "Cap nhat bai tap thanh cong", problem));
    }

    // Delete problem
    @PreAuthorize("hasAnyAuthority('ADMIN', 'CREATOR')")
    @DeleteMapping("/problem/{id}")
    public ResponseEntity<Response<String>> deleteProblem(@PathVariable Long id) {
        problemService.deleteProblem(id);
        return ResponseEntity.ok(new Response<String>("success", "Xoa bai tap thanh cong", null));
    }
}
