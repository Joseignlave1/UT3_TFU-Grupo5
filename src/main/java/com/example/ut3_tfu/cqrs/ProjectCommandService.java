package com.example.ut3_tfu.cqrs;

import com.example.ut3_tfu.DTOs.project.ProjectRequestDTO;
import com.example.ut3_tfu.DTOs.project.ProjectResponseDTO;
import com.example.ut3_tfu.cqrs.events.ProjectCreatedEvent;
import com.example.ut3_tfu.models.Project;
import com.example.ut3_tfu.repositories.ProjectRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servicio simplificado CQRS para comandos de escritura de proyectos.
 */
@Service
@Transactional
public class ProjectCommandService {
    
    private final ProjectRepository projectRepository;
    private final ApplicationEventPublisher eventPublisher;
    
    public ProjectCommandService(ProjectRepository projectRepository, 
                                ApplicationEventPublisher eventPublisher) {
        this.projectRepository = projectRepository;
        this.eventPublisher = eventPublisher;
    }
    
    public ProjectResponseDTO createProject(ProjectRequestDTO dto) {
        // Crear y guardar proyecto
        Project project = new Project(dto.getName(), dto.getDescription());
        project = projectRepository.save(project);
        
        // Publicar evento para actualizar vista
        eventPublisher.publishEvent(
            new ProjectCreatedEvent(this, project.getId(), project.getName(), project.getDescription())
        );
        
        // Retornar respuesta
        ProjectResponseDTO response = new ProjectResponseDTO();
        response.setId(project.getId());
        response.setName(project.getName());
        response.setDescription(project.getDescription());
        
        return response;
    }
}
