package org.example.neptuneojserver.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponseDTO {
    private Long id;
    private String username;
    private String fullName;
    private String role;
    private double point;
    private Long numberOfProblems;
    private Long rank;
    private String token;
}
