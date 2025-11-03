package com.example.ut3_tfu.cqrs;

import com.example.ut3_tfu.exceptions.ResourceNotFoundException;
import com.example.ut3_tfu.views.ProjectView;
import com.example.ut3_tfu.views.ProjectViewRepository;
import com.example.ut3_tfu.views.TaskView;
import com.example.ut3_tfu.views.TaskViewRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Servicio simplificado CQRS para consultas de lectura de proyectos.
 * Lee directamente desde las vistas materializadas.
 */
@Service
@Transactional(readOnly = true)
public class ProjectQueryService {
    
    private final ProjectViewRepository projectViewRepository;
    private final TaskViewRepository taskViewRepository;
    
    public ProjectQueryService(ProjectViewRepository projectViewRepository,
                              TaskViewRepository taskViewRepository) {
        this.projectViewRepository = projectViewRepository;
        this.taskViewRepository = taskViewRepository;
    }
    
    public List<ProjectView> getAllProjects() {
        return projectViewRepository.findAll();
    }
    
    public ProjectView getProjectById(Long id) {
        return projectViewRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Project not found: " + id));
    }
    
    public List<TaskView> getTasksByProject(Long projectId) {
        return taskViewRepository.findByProjectId(projectId);
    }
    
    public List<ProjectView> getProjectsWithTasks() {
        return projectViewRepository.findAllWithTasks();
    }
}
