package org.example.neptuneojserver.models;

import jakarta.persistence.*;

import java.time.ZonedDateTime;
import java.util.List;

@Entity
@Table(name = "contests")
public class Contest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String title;

    @Column(columnDefinition = "TEXT")
    private String body;

    @Column(name = "image_url", columnDefinition = "TEXT")
    private String imageUrl;

    @Column(name = "contest_key", columnDefinition = "TEXT")
    private String contestKey;

    @Column(name = "start_time", nullable = false)
    private ZonedDateTime startTime;

    @Column(name = "end_time", nullable = false)
    private ZonedDateTime endTime;

    @Column(name = "ranking_freeze_time")
    private ZonedDateTime rankingFreezeTime;

    @Column(name = "created_at", nullable = false)
    private ZonedDateTime createdAt;

    // Getters and Setters

    @OneToMany(mappedBy = "contest")
    private List<ContestProblem> contestProblems;

    @OneToMany(mappedBy = "contest")
    private List<Submission> submissions;

    @OneToMany(mappedBy = "contest")
    private List<ContestRanking> contestRankings;

    // Getters and Setters for ContestProblem, Submission, and ContestRanking
}
