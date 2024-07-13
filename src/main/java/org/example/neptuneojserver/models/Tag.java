package org.example.neptuneojserver.models;

import jakarta.persistence.*;

import java.time.ZonedDateTime;
import java.util.List;

@Entity
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

    @OneToMany(mappedBy = "tag")
    private List<ProblemTag> problemTags;

    // Getter and Setter for ProblemTag
}

