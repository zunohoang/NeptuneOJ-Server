package org.example.neptuneojserver.models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.ZonedDateTime;
import java.util.List;

@Entity
@Data
@Table(name = "tags")
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(name = "created_at", nullable = false)
    private ZonedDateTime createdAt;

    // Getters and Setters

    @OneToMany(mappedBy = "tag", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProblemTag> problemTags;

    // Getter and Setter for ProblemTag

    public Tag(String title) {
        this.title = title;
        this.createdAt = ZonedDateTime.now();
    }

    public Tag() {

    }
}

