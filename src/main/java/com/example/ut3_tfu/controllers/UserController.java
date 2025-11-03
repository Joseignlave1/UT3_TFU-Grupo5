package com.example.ut3_tfu.controllers;

import com.example.ut3_tfu.DTOs.user.UserRequestDTO;
import com.example.ut3_tfu.DTOs.user.UserResponseDTO;
import com.example.ut3_tfu.cqrs.UserCommandService;
import com.example.ut3_tfu.cqrs.UserQueryService;
import com.example.ut3_tfu.views.UserView;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador de usuarios con CQRS.
 * - POST: Usa CommandService (escritura)
 * - GET: Usa QueryService (lectura desde vistas materializadas)
 */
@RestController
@RequestMapping("/api/users")
public class UserController {
    
    private final UserCommandService commandService;
    private final UserQueryService queryService;
    
    public UserController(UserCommandService commandService,
                        UserQueryService queryService) {
        this.commandService = commandService;
        this.queryService = queryService;
    }

    @PostMapping
    public ResponseEntity<UserResponseDTO> create(@RequestBody UserRequestDTO dto) {
        UserResponseDTO created = commandService.createUser(dto);
        return ResponseEntity.status(201).body(created);
    }

    @GetMapping
    public ResponseEntity<List<UserView>> findAll() {
        return ResponseEntity.ok(queryService.getAllUsers());
    }
}
