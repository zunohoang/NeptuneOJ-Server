package org.example.neptuneojserver.repositories;

import org.example.neptuneojserver.models.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {
    Optional<Tag> findByTitle(String title);


}
