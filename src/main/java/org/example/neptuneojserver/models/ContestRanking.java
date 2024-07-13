package org.example.neptuneojserver.models;

import jakarta.persistence.*;

import java.time.ZonedDateTime;;

@Entity
@Table(name = "contest_rankings")
public class ContestRanking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "contest_id", nullable = false)
    private Long contestId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long rank;

    @Column(name = "join_time", nullable = false)
    private ZonedDateTime joinTime;

    @Column(name = "leave_time")
    private ZonedDateTime leaveTime;

    // Getters and Setters

    @ManyToOne
    @JoinColumn(name = "contest_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Contest contest;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", insertable = false, updatable = false)
    private User user;

    // Getters and Setters for Contest and User
}

