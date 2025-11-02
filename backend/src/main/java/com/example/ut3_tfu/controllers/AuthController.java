package com.example.ut3_tfu.controllers;

import com.example.ut3_tfu.DTOs.auth.UserLoginRequestDTO;
import com.example.ut3_tfu.DTOs.auth.UserRegisterRequestDTO;
import com.example.ut3_tfu.DTOs.auth.UserResponseDTO;
import com.example.ut3_tfu.services.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register(@Valid @RequestBody UserRegisterRequestDTO dto) {
        UserResponseDTO created = authService.register(dto);
        return ResponseEntity.status(201).body(created);
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponseDTO> login(@Valid @RequestBody UserLoginRequestDTO dto) {
        UserResponseDTO logged = authService.login(dto);
        return ResponseEntity.ok(logged);
    }
}
