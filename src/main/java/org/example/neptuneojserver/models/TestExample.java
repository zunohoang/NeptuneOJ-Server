package org.example.neptuneojserver.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "test_examples")
public class TestExample {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String input;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String output;

    @Column(columnDefinition = "TEXT")
    private String description;

    // Getters and Setters

    @ManyToOne
    @JoinColumn(name = "problem_id", referencedColumnName = "id")
    private Problem problem;

    // Getter and Setter for Problem
}
