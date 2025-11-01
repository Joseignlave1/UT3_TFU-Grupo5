package com.example.ut3_tfu.controllers;

import com.example.ut3_tfu.DTOs.task.TaskRequestDTO;
import com.example.ut3_tfu.DTOs.task.TaskResponseDTO;
import com.example.ut3_tfu.cqrs.TaskCommandService;
import com.example.ut3_tfu.cqrs.TaskQueryService;
import com.example.ut3_tfu.views.TaskView;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador de tareas con CQRS.
 * - POST/PUT: Usa CommandService (escritura)
 * - GET: Usa QueryService (lectura desde vistas materializadas)
 */
@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    
    private final TaskCommandService commandService;
    private final TaskQueryService queryService;
    
    public TaskController(TaskCommandService commandService,
                        TaskQueryService queryService) {
        this.commandService = commandService;
        this.queryService = queryService;
    }

    @PostMapping
    public ResponseEntity<TaskResponseDTO> create(@RequestBody TaskRequestDTO dto) {
        return ResponseEntity.status(201).body(commandService.createTask(dto));
    }

    @PutMapping("{id}")
    public ResponseEntity<TaskResponseDTO> update(@PathVariable Long id, @RequestBody TaskRequestDTO dto) {
        return ResponseEntity.ok(commandService.updateTask(id, dto));
    }

    @GetMapping
    public ResponseEntity<List<TaskView>> findAll() {
        return ResponseEntity.ok(queryService.getAllTasks());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TaskView>> findByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(queryService.getTasksByUser(userId));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<TaskView>> findByStatus(@PathVariable String status) {
        return ResponseEntity.ok(queryService.getTasksByStatus(status));
    }

    @PostMapping("{taskId}/users/{userId}")
    public ResponseEntity<Void> assignUser(@PathVariable Long taskId, @PathVariable Long userId) {
        commandService.assignUserToTask(taskId, userId);
        return ResponseEntity.status(204).build();
    }
}
