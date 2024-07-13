package org.example.neptuneojserver.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "testcases")
public class Testcase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String input;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String output;

//    @Column(name = "problem_id", nullable = false)
//    private Long problemId;

    @Column(name = "index_in_problem", nullable = false)
    private Long indexInProblem;

    // Getters and Setters

    @ManyToOne
    @JoinColumn(name = "problem_id", referencedColumnName = "id")
    private Problem problem;

    // Getter and Setter for Problem
}
