package org.example.neptuneojserver.controllers;

import lombok.AllArgsConstructor;
import org.example.neptuneojserver.dto.Response;
import org.example.neptuneojserver.models.Contest;
import org.example.neptuneojserver.services.ContestService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1")
public class ContestController {

    private final ContestService contestService;

    @GetMapping("/contests")
    public ResponseEntity<Response<?>> getContests(@RequestParam(value = "type", defaultValue = "current") String type,
                                                    @RequestParam(value = "page", defaultValue = "1") int page,
                                                    @RequestParam(value = "size", defaultValue = "10") int size) {
        return ResponseEntity.ok(new Response<>("success", "Get contest by " + type,
                contestService.getContests(type, page, size)));
    }

    @PostMapping("/contests")
    public ResponseEntity<Response<?>> createContest(Contest contest) {
        return ResponseEntity.ok(new Response<>("success", "Create contest", contestService.createContest(contest)));
    }

    @GetMapping("/contests/{id}")
    public ResponseEntity<Response<?>> getContest(@PathVariable Long id) {
        return ResponseEntity.ok(new Response<>("success", "Get contest by id", contestService.getContest(id)));
    }

    @DeleteMapping("/contests/{id}")
    public ResponseEntity<Response<?>> deleteContest(@PathVariable Long id) {
        contestService.deleteContest(id);
        return ResponseEntity.ok(new Response<>("success", "Delete contest", null));
    }

    @PutMapping("/contests/{id}")
    public ResponseEntity<Response<?>> updateContest(@RequestBody Contest contest) {
        contestService.updateContest(contest);
        return ResponseEntity.ok(new Response<>("success", "Update contest", null));
    }

}
