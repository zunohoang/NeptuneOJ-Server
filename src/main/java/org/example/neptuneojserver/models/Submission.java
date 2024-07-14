package org.example.neptuneojserver.models;

import jakarta.persistence.*;

import java.time.ZonedDateTime;

@Entity
@Table(name = "submissions")
public class Submission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "problem_id", nullable = false)
    private Long problemId;

    @Column(name = "file_name", nullable = false, columnDefinition = "TEXT")
    private String fileName;

    @Column(name = "source_code", nullable = false, columnDefinition = "TEXT")
    private String sourceCode;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String result;

    @Column(nullable = false, length = 255)
    private String status;

    @Column(name = "created_at", nullable = false)
    private ZonedDateTime createdAt;

    @Column(name = "contest_id", nullable = false)
    private Long contestId;

    // Getters and Setters

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", insertable = false, updatable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "problem_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Problem problem;

    @ManyToOne
    @JoinColumn(name = "contest_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Contest contest;

    // Getters and Setters for User, Problem, and Contest
}
