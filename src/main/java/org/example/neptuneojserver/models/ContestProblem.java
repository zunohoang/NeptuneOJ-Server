package org.example.neptuneojserver.models;

import jakarta.persistence.*;
@Entity
@Table(name = "contest_problems")
public class ContestProblem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "contest_id", nullable = false)
    private Long contestId;

    @Column(name = "problem_id", nullable = false)
    private Long problemId;

    // Getters and Setters

    @ManyToOne
    @JoinColumn(name = "contest_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Contest contest;

    @ManyToOne
    @JoinColumn(name = "problem_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Problem problem;

    // Getters and Setters for Contest and Problem
}

