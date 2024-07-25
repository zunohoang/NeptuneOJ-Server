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
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String username;

    @JsonIgnore
    @Column(nullable = false, length = 255)
    private String password;

    @Column(name = "fullname", nullable = false)
    private String fullName;

    @Column(nullable = false)
    private String role;

    @Column(nullable = false)
    private Float point;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "number_of_problems", nullable = false)
    private Long numberOfProblems;

    @Column(nullable = false)
    private Long rank;

    @Column(name = "img_url", columnDefinition = "TEXT")
    private String imgUrl;

    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY)
    private List<Problem> authorProblems;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Submission> submissions;

    @Column(name = "created_at", nullable = false)
    private ZonedDateTime createdAt;

    public User(String username, String password, String fullName, Long rank) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.role = "user";
        this.point = 0F;
        this.numberOfProblems = 0L;
        this.rank = 0L;
        this.imgUrl = null;
        this.createdAt = ZonedDateTime.now();
        this.rank = rank;
    }

    public User(String username) {
        this.username = username;
    }

    public User(Long id) {
        this.id = id;
    }

    public User(Long id, String username, String fullName) {
        this.id = id;
        this.username = username;
        this.fullName = fullName;
    }
}
