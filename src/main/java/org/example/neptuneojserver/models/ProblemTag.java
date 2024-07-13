package org.example.neptuneojserver.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.time.ZonedDateTime;

@Entity
@Table(name = "problem_tags")
public class ProblemTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "problem_id", nullable = false)
    private Long problemId;

    @Column(name = "tag_id", nullable = false)
    private Long tagId;

    @Column(name = "created_at", nullable = false)
    private ZonedDateTime createdAt;

    // Getters and Setters

    @ManyToOne
    @JoinColumn(name = "problem_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Problem problem;

    @ManyToOne
    @JoinColumn(name = "tag_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Tag tag;

    // Getters and Setters for Problem and Tag
}

