package org.example.neptuneojserver.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RegisterRequestDTO {
    private String username;
    private String password;
    private String fullName;
}
