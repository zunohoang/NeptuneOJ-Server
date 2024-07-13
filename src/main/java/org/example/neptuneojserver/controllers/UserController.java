package org.example.neptuneojserver.controllers;

import lombok.AllArgsConstructor;
import org.example.neptuneojserver.dto.auth.LoginRequestDTO;
import org.example.neptuneojserver.dto.auth.RegisterRequestDTO;
import org.example.neptuneojserver.dto.auth.LoginResponseDTO;
import org.example.neptuneojserver.dto.Response;
import org.example.neptuneojserver.models.User;
import org.example.neptuneojserver.services.JwtService;
import org.example.neptuneojserver.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1")
public class UserController {
    private final UserService userService;
    private final JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<Response> loginByUsernameAndPassword(@RequestBody LoginRequestDTO loginRequest) {
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        User user = userService.loginByUsernameAndPassword(username, password);
        if(user != null) {
            LoginResponseDTO loginResponse = new LoginResponseDTO(
                user.getId(),
                user.getUsername(),
                user.getFullName(),
                user.getRole(),
                user.getPoint(),
                user.getNumberOfProblems(),
                user.getRank(),
                jwtService.generateToken(user)
            );
            return ResponseEntity.ok(new Response<LoginResponseDTO>("success", "Dang nhap thanh cong", loginResponse));
        } else return ResponseEntity.status(401).build();
    }

    @PostMapping("/register")
    public ResponseEntity<Response> register(@RequestBody RegisterRequestDTO registerRequest) {
        if(userService.register(registerRequest) != null) {
            return ResponseEntity.ok(new Response<RegisterRequestDTO>("success", "Dang ky thanh cong", registerRequest));
        } else return ResponseEntity.status(409).build();
    }
}
