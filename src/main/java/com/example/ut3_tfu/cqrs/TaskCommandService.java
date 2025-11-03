package com.example.ut3_tfu.cqrs;

import com.example.ut3_tfu.DTOs.task.TaskRequestDTO;
import com.example.ut3_tfu.DTOs.task.TaskResponseDTO;
import com.example.ut3_tfu.cqrs.events.TaskCreatedEvent;
import com.example.ut3_tfu.cqrs.events.TaskUpdatedEvent;
import com.example.ut3_tfu.cqrs.events.UserAssignedToTaskEvent;
import com.example.ut3_tfu.exceptions.ResourceNotFoundException;
import com.example.ut3_tfu.models.Project;
import com.example.ut3_tfu.models.Task;
import com.example.ut3_tfu.models.User;
import com.example.ut3_tfu.repositories.ProjectRepository;
import com.example.ut3_tfu.repositories.TaskRepository;
import com.example.ut3_tfu.repositories.UserRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servicio simplificado CQRS para comandos de escritura de tareas.
 */
@Service
@Transactional
public class TaskCommandService {
    
    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;
    
    public TaskCommandService(TaskRepository taskRepository,
                             ProjectRepository projectRepository,
                             UserRepository userRepository,
                             ApplicationEventPublisher eventPublisher) {
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.eventPublisher = eventPublisher;
    }
    
    public TaskResponseDTO createTask(TaskRequestDTO dto) {
        // Validar proyecto
        Project project = projectRepository.findById(dto.getProjectId())
            .orElseThrow(() -> new ResourceNotFoundException("Project not found: " + dto.getProjectId()));
        
        // Crear y guardar tarea
        Task task = new Task(dto.getTitle(), dto.getStatus(), project);
        task = taskRepository.save(task);
        
        // Publicar evento
        eventPublisher.publishEvent(
            new TaskCreatedEvent(this, task.getId(), task.getTitle(), 
                               task.getStatus(), project.getId())
        );
        
        // Retornar respuesta
        TaskResponseDTO response = new TaskResponseDTO();
        response.setId(task.getId());
        response.setTitle(task.getTitle());
        response.setStatus(task.getStatus());
        response.setProjectId(project.getId());
        
        return response;
    }
    
    public TaskResponseDTO updateTask(Long taskId, TaskRequestDTO dto) {
        // Buscar tarea
        Task task = taskRepository.findById(taskId)
            .orElseThrow(() -> new ResourceNotFoundException("Task not found: " + taskId));
        
        // Actualizar
        task.setTitle(dto.getTitle());
        task.setStatus(dto.getStatus());
        task = taskRepository.save(task);
        
        // Publicar evento
        eventPublisher.publishEvent(
            new TaskUpdatedEvent(this, task.getId(), task.getTitle(), task.getStatus())
        );
        
        // Retornar respuesta
        TaskResponseDTO response = new TaskResponseDTO();
        response.setId(task.getId());
        response.setTitle(task.getTitle());
        response.setStatus(task.getStatus());
        response.setProjectId(task.getProject().getId());
        
        return response;
    }
    
    public void assignUserToTask(Long taskId, Long userId) {
        // Buscar tarea y usuario
        Task task = taskRepository.findById(taskId)
            .orElseThrow(() -> new ResourceNotFoundException("Task not found: " + taskId));
        
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));
        
        // Asignar
        task.addUser(user);
        taskRepository.save(task);
        
        // Publicar evento
        eventPublisher.publishEvent(
            new UserAssignedToTaskEvent(this, taskId, userId)
        );
    }
}
