package org.example.neptuneojserver.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
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

    @Column(name = "number_of_participants", nullable = false)
    private Long numberOfParticipants;

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

    public Contest(Long id, String title) {
        this.id = id;
        this.title = title;
    }

    // Getters and Setters for ContestProblem, Submission, and ContestRanking
}
