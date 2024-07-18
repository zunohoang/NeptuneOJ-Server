package org.example.neptuneojserver.services;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.example.neptuneojserver.models.Tag;
import org.example.neptuneojserver.repositorys.ProblemRepository;
import org.example.neptuneojserver.repositorys.TagRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;

@Service
@AllArgsConstructor

public class TagService {

    private final TagRepository tagRepository;

    public Tag createTag(String title) {
        Tag tag = new Tag();
        tag.setTitle(title);
        tag.setCreatedAt(ZonedDateTime.now());
        return tagRepository.save(tag);
    }

    public List<Tag> getTags() {
        return tagRepository.findAll();
    }

    public void deleteTag(Long id) {
        Tag tag = tagRepository.findById(id).orElseThrow(() -> new RuntimeException("Tag not found"));
        tagRepository.delete(tag);
    }

    public Tag updateTag(Long id, String title) {
        Tag tag = tagRepository.findById(id).orElseThrow(() -> new RuntimeException("Tag not found"));
        tag.setTitle(title);
        return tagRepository.save(tag);
    }
}
