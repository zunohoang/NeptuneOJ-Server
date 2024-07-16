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
@Table(name = "submissions")
public class Submission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "source_code", columnDefinition = "TEXT")
    private String sourceCode;

    @Column(columnDefinition = "TEXT")
    private String result;

    @Column(length = 255)
    private String status;

    @Column(name = "created_at")
    private ZonedDateTime createdAt;

    // Getters and Setters

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "problem_id", referencedColumnName = "id")
    private Problem problem;

    @ManyToOne
    @JoinColumn(name = "contest_id", referencedColumnName = "id")
    private Contest contest;

    @OneToMany(mappedBy = "submission", cascade = CascadeType.ALL)
    private List<JudgeStatus> judgeStatuses;

    public Submission(Long submissionId) {
        this.id = submissionId;
    }

    // Getters and Setters for User, Problem, and Contest
}
