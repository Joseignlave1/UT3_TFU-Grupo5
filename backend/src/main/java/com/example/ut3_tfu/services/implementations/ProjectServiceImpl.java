package com.example.ut3_tfu.services.implementations;

import com.example.ut3_tfu.DTOs.project.*;
import com.example.ut3_tfu.DTOs.task.*;
import com.example.ut3_tfu.DTOs.user.*;

import com.example.ut3_tfu.exceptions.ResourceNotFoundException;

import com.example.ut3_tfu.models.Project;
import com.example.ut3_tfu.models.Task;

import com.example.ut3_tfu.repositories.ProjectRepository;

import com.example.ut3_tfu.services.interfaces.ProjectService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepository repo;

    public ProjectServiceImpl(ProjectRepository repo) { this.repo = repo; }

    @Override
    @Transactional
    public ProjectResponseDTO create(ProjectRequestDTO dto) {
        Project p = new Project(dto.getName(), dto.getDescription());
        p = repo.save(p);
        return toResponseDTO(p);
    }

    @Override
    public Set<ProjectResponseDTO> findAll() {
        return repo.findAll().stream().map(this::toResponseDTO).collect(Collectors.toSet());
    }

    @Override
    public ProjectResponseDTO findById(Long id) {
        return repo.findById(id).map(this::toResponseDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found: " + id));
    }

    @Override
    public Set<TaskResponseDTO> findTasksByProject(Long id) {
        Project p = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Project not found: " + id));
        return p.getTasks().stream().map(this::taskToDto).collect(Collectors.toSet());
    }

    public ProjectResponseDTO toResponseDTO(Project p) {
        ProjectResponseDTO dto = new ProjectResponseDTO();
        dto.setId(p.getId()); dto.setName(p.getName()); dto.setDescription(p.getDescription());
        dto.setTasks(p.getTasks().stream().map(this::taskToDto).collect(Collectors.toSet()));
        return dto;
    }

    private TaskResponseDTO taskToDto(Task t) {
        TaskResponseDTO tr = new TaskResponseDTO();
        tr.setId(t.getId()); tr.setTitle(t.getTitle()); tr.setStatus(t.getStatus());
        tr.setProjectId(t.getProject() != null ? t.getProject().getId() : null);
        tr.setUsers(t.getUsers().stream().map(u -> {
            UserResponseDTO ur = new UserResponseDTO();
            ur.setId(u.getId()); ur.setUsername(u.getUsername()); ur.setEmail(u.getEmail());
            return ur;
        }).collect(Collectors.toSet()));
        return tr;
    }
}