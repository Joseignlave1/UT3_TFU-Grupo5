package com.example.ut3_tfu.controllers;

import com.example.ut3_tfu.DTOs.project.ProjectRequestDTO;
import com.example.ut3_tfu.DTOs.project.ProjectResponseDTO;
import com.example.ut3_tfu.cqrs.ProjectCommandService;
import com.example.ut3_tfu.cqrs.ProjectQueryService;
import com.example.ut3_tfu.views.ProjectView;
import com.example.ut3_tfu.views.TaskView;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador de proyectos con CQRS.
 * - POST: Usa CommandService (escritura)
 * - GET: Usa QueryService (lectura desde vistas materializadas)
 */
@RestController
@RequestMapping("/api/projects")
public class ProjectController {
    
    private final ProjectCommandService commandService;
    private final ProjectQueryService queryService;
    
    public ProjectController(ProjectCommandService commandService,
                           ProjectQueryService queryService) {
        this.commandService = commandService;
        this.queryService = queryService;
    }

    @PostMapping
    public ResponseEntity<ProjectResponseDTO> create(@RequestBody ProjectRequestDTO dto) {
        return ResponseEntity.status(201).body(commandService.createProject(dto));
    }

    @GetMapping
    public ResponseEntity<List<ProjectView>> findAll() {
        return ResponseEntity.ok(queryService.getAllProjects());
    }

    @GetMapping("{id}")
    public ResponseEntity<ProjectView> findById(@PathVariable Long id) {
        return ResponseEntity.ok(queryService.getProjectById(id));
    }

    @GetMapping("{id}/tasks")
    public ResponseEntity<List<TaskView>> findTasksByProject(@PathVariable Long id) {
        return ResponseEntity.ok(queryService.getTasksByProject(id));
    }
}
