package org.example.neptuneojserver.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "testcases")
public class Testcase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String input;

    @Column(columnDefinition = "TEXT")
    private String output;

//    @Column(name = "problem_id", nullable = false)
//    private Long problemId;

    @Column(name = "index_in_problem")
    private Long indexInProblem;

    // Getters and Setters

    @ManyToOne
    @JoinColumn(name = "problem_id", referencedColumnName = "id")
    private Problem problem;

    public Testcase(Long id) {
        this.id = id;
    }

    // Getter and Setter for Problem
}
