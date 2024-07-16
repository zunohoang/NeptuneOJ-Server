package org.example.neptuneojserver.controllers;

import lombok.AllArgsConstructor;
import org.example.neptuneojserver.dto.auth.LoginRequestDTO;
import org.example.neptuneojserver.dto.auth.RegisterRequestDTO;
import org.example.neptuneojserver.dto.auth.LoginResponseDTO;
import org.example.neptuneojserver.dto.Response;
import org.example.neptuneojserver.dto.user.UserDTO;
import org.example.neptuneojserver.models.User;
import org.example.neptuneojserver.services.JwtService;
import org.example.neptuneojserver.services.UserService;
import org.hibernate.query.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1")
public class UserController {
    private final UserService userService;
    private final JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<Response<?>> loginByUsernameAndPassword(@RequestBody LoginRequestDTO loginRequest) {
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
    public ResponseEntity<Response<?>> register(@RequestBody RegisterRequestDTO registerRequest) {
        if(userService.register(registerRequest) != null) {
            return ResponseEntity.ok(new Response<RegisterRequestDTO>("success", "Dang ky thanh cong", registerRequest));
        } else return ResponseEntity.status(409).build();
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<?> getUserById(@PathVariable String username) {
        return ResponseEntity.ok(new Response<>("success", "Lay thong tin user thanh cong", userService.getUserByUsername(username)));
    }

    @GetMapping("/users")
    public ResponseEntity<Response<?>> getUsers(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size){
        return ResponseEntity.ok(new Response<>("success", "Lay thong tin tat ca user thanh cong", userService.getUsers(page, size)));
    }

    // chi co the sua ten va mo ta
    @PutMapping("/user/{username}")
    public ResponseEntity<Response<?>> updateUser(@PathVariable String username, @RequestBody UserDTO userDTO) {
        userService.updateUser(username, userDTO);
        return ResponseEntity.ok(new Response<>("success", "Cap nhat thong tin user thanh cong", userDTO));
    }

    @DeleteMapping("/user/{username}")
    public ResponseEntity<Response<?>> deleteUser(@PathVariable String username) {
        userService.deleteUser(username);
        return ResponseEntity.ok(new Response<>("success", "Xoa user thanh cong", null));
    }
}
