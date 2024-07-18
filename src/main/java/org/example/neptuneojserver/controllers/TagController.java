package org.example.neptuneojserver.controllers;

import lombok.AllArgsConstructor;
import org.example.neptuneojserver.dto.Response;
import org.example.neptuneojserver.dto.tags.NewTagDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.example.neptuneojserver.services.TagService;

@RestController
@RequestMapping("/api/v1")
public class TagController {

    private final TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping("/tags")
    public ResponseEntity<Response<?>> getTags() {

        return ResponseEntity.ok(new Response<>("success", "Lay thanh cong cac van de", tagService.getTags()));
    }

    @PostMapping("/tag")
    public ResponseEntity<Response<?>> createTag(@RequestBody NewTagDTO newTagDTO) {
        return ResponseEntity.ok(new Response<>("success", "Tao thanh cong tag", tagService.createTag(newTagDTO.getTitle())));
    }

    @DeleteMapping("/tag/{id}")
    public ResponseEntity<Response<?>> deleteTag(@PathVariable Long id) {
        tagService.deleteTag(id);
        return ResponseEntity.ok(new Response<>("success", "Xoa thanh cong tag", null));
    }

    @PutMapping("/tag/{id}")
    public ResponseEntity<Response<?>> updateTag(@PathVariable Long id, @RequestBody NewTagDTO newTagDTO) {
        return ResponseEntity.ok(new Response<>("success", "Cap nhat thanh cong tag", tagService.updateTag(id, newTagDTO.getTitle())));
    }
}
