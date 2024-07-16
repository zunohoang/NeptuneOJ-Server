package org.example.neptuneojserver.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @JsonIgnore
    @Column(name = "source_code", columnDefinition = "TEXT")
    private String sourceCode;

    @Column(columnDefinition = "TEXT")
    private String result;

    @Column(length = 255)
    private String status;

    @Column(name = "created_at")
    private ZonedDateTime createdAt;

    // Getters and Setters

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "problem_id", referencedColumnName = "id")
    private Problem problem;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "contest_id", referencedColumnName = "id")
    private Contest contest;

    @JsonIgnore
    @OneToMany(mappedBy = "submission", cascade = CascadeType.ALL)
    private List<JudgeStatus> judgeStatuses;

    public Submission(Long submissionId) {
        this.id = submissionId;
    }

    // Getters and Setters for User, Problem, and Contest
}
