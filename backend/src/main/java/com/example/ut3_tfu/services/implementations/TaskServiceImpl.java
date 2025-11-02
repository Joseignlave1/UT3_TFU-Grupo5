package com.example.ut3_tfu.services.implementations;

import com.example.ut3_tfu.DTOs.task.TaskRequestDTO;
import com.example.ut3_tfu.DTOs.task.TaskResponseDTO;
import com.example.ut3_tfu.DTOs.user.UserResponseDTO;

import com.example.ut3_tfu.exceptions.ResourceNotFoundException;

import com.example.ut3_tfu.models.*;

import com.example.ut3_tfu.repositories.*;

import com.example.ut3_tfu.services.interfaces.TaskService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepo;
    private final ProjectRepository projectRepo;
    private final UserRepository userRepo;

    public TaskServiceImpl(TaskRepository taskRepo, ProjectRepository projectRepo, UserRepository userRepo) {
        this.taskRepo = taskRepo; this.projectRepo = projectRepo; this.userRepo = userRepo;
    }

    @Override
    @Transactional
    public TaskResponseDTO create(TaskRequestDTO dto) {
        Project project = projectRepo.findById(dto.getProjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Project not found: " + dto.getProjectId()));
        Task t = new Task(dto.getTitle(), dto.getStatus(), project);
        t = taskRepo.save(t);
        // optional: add to project tasks set
        project.getTasks().add(t);
        return toResponseDTO(t);
    }

    @Override
    public TaskResponseDTO findById(Long id) {
        Task t = taskRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Task not found: " + id));
        return toResponseDTO(t);
    }

    @Override
    @Transactional
    public void assignUser(Long taskId, Long userId) {
        Task t = taskRepo.findById(taskId).orElseThrow(() -> new ResourceNotFoundException("Task not found: " + taskId));
        User u = userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));
        if (!t.getUsers().contains(u)) {
            t.addUser(u);
            taskRepo.save(t);
        }
    }

    public TaskResponseDTO toResponseDTO(Task t) {
        TaskResponseDTO dto = new TaskResponseDTO();
        dto.setId(t.getId());
        dto.setTitle(t.getTitle());
        dto.setStatus(t.getStatus());
        dto.setProjectId(t.getProject() != null ? t.getProject().getId() : null);
        dto.setUsers(t.getUsers().stream().map(u -> {
            UserResponseDTO ur = new UserResponseDTO();
            ur.setId(u.getId()); ur.setUsername(u.getUsername()); ur.setEmail(u.getEmail());
            return ur;
        }).collect(Collectors.toSet()));
        return dto;
    }
}