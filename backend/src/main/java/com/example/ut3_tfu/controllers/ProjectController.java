package com.example.ut3_tfu.controllers;

import com.example.ut3_tfu.DTOs.project.ProjectRequestDTO;
import com.example.ut3_tfu.DTOs.project.ProjectResponseDTO;
import com.example.ut3_tfu.DTOs.task.TaskResponseDTO;

import com.example.ut3_tfu.services.interfaces.ProjectService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {
    private final ProjectService svc;
    public ProjectController(ProjectService svc) { this.svc = svc; }

    @PostMapping
    public ResponseEntity<ProjectResponseDTO> create(@RequestBody ProjectRequestDTO dto) {
        return ResponseEntity.status(201).body(svc.create(dto));
    }

    @GetMapping
    public ResponseEntity<Set<ProjectResponseDTO>> findAll() {
        return ResponseEntity.ok(svc.findAll());
    }

    @GetMapping("{id}")
    public ResponseEntity<ProjectResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(svc.findById(id));
    }

    @GetMapping("{id}/tasks")
    public ResponseEntity<Set<TaskResponseDTO>> findTasksByProject(@PathVariable Long id) {
        return ResponseEntity.ok(svc.findTasksByProject(id));
    }
}
