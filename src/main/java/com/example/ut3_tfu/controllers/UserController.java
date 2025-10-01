package com.example.ut3_tfu.controllers;

import com.example.ut3_tfu.DTOs.user.UserRequestDTO;
import com.example.ut3_tfu.DTOs.user.UserResponseDTO;

import com.example.ut3_tfu.services.interfaces.UserService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService svc;
    public UserController(UserService svc) { this.svc = svc; }

    @PostMapping
    public ResponseEntity<UserResponseDTO> create(@RequestBody UserRequestDTO dto) {
        UserResponseDTO created = svc.create(dto);
        return ResponseEntity.status(201).body(created);
    }

    @GetMapping
    public ResponseEntity<Set<UserResponseDTO>> findAll() {
        return ResponseEntity.ok(svc.findAll());
    }

    @GetMapping("{id}")
    public ResponseEntity<UserResponseDTO> findById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(svc.findById(id));
    }
}
