package com.example.ut3_tfu.services.interfaces;

import com.example.ut3_tfu.DTOs.project.ProjectRequestDTO;
import com.example.ut3_tfu.DTOs.project.ProjectResponseDTO;
import com.example.ut3_tfu.DTOs.task.TaskResponseDTO;

import java.util.Set;

public interface ProjectService {
    ProjectResponseDTO create(ProjectRequestDTO dto);

    Set<ProjectResponseDTO> findAll();

    ProjectResponseDTO findById(Long id);
    
    Set<TaskResponseDTO> findTasksByProject(Long id);
}