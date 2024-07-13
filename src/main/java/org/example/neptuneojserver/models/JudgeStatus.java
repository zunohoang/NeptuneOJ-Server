package org.example.neptuneojserver.models;

import jakarta.persistence.*;
@Entity
@Table(name = "judge_statuses")
public class JudgeStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "submission_id", nullable = false)
    private Long submissionId;

    @Column(name = "testcase_id", nullable = false)
    private Long testcaseId;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String status;

    // Getters and Setters

    @ManyToOne
    @JoinColumn(name = "submission_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Submission submission;

    @ManyToOne
    @JoinColumn(name = "testcase_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Testcase testcase;

    // Getters and Setters for Submission and Testcase
}
