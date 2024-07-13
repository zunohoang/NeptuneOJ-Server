package org.example.neptuneojserver.models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.ZonedDateTime;
import java.util.List;

@Entity
@Data
@Table(name = "problems")
public class Problem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String body;

    @Column(nullable = false)
    private Float point;

    @Column(name = "number_accept", nullable = false)
    private Long numberAccept;

    @Column(name = "time_limit", nullable = false)
    private Float timeLimit;

    @Column(nullable = false)
    private Long memory;

    @Column(name = "created_at", nullable = false)
    private ZonedDateTime createdAt;

    @Column(nullable = false)
    private boolean hidden;

    // Getters and Setters

    @ManyToOne
    @JoinColumn(name = "author_id", referencedColumnName = "id")
    private User author;

    @OneToMany(mappedBy = "problem", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Testcase> testcases;

    @OneToMany(mappedBy = "problem", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TestExample> testExamples;

    @OneToMany(mappedBy = "problem")
    private List<Submission> submissions;

    @OneToMany(mappedBy = "problem")
    private List<ProblemTag> problemTags;

    // Getters and Setters for Author, Testcase, Submission, and ProblemTag
}
