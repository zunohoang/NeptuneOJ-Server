package org.example.neptuneojserver.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "judge_statuses")
public class JudgeStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String status;

    @Column(columnDefinition = "TEXT")
    private String input;

    @Column(columnDefinition = "TEXT")
    private String output;

    @Column(columnDefinition = "TEXT")
    private String expectedOutput;

    @Column(name = "time_run")
    private Float timeRun;

    @Column(name = "memory_run")
    private Float memoryRun;


    @Column(columnDefinition = "TEXT")
    private Long index_in_testcase;

    // Getters and Setters

    @ManyToOne
    @JoinColumn(name = "submission_id", referencedColumnName = "id")
    private Submission submission;

    @ManyToOne
    @JoinColumn(name = "testcase_id", referencedColumnName = "id")
    private Testcase testcase;


    // Getters and Setters for Submission and Testcase
}
