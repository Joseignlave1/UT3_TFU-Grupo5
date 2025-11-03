package com.example.ut3_tfu.cqrs.events;

import com.example.ut3_tfu.views.ProjectView;
import com.example.ut3_tfu.views.ProjectViewRepository;
import com.example.ut3_tfu.views.TaskView;
import com.example.ut3_tfu.views.TaskViewRepository;
import com.example.ut3_tfu.views.UserViewRepository;
import com.example.ut3_tfu.repositories.ProjectRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Listener que escucha eventos del dominio y actualiza las vistas materializadas.
 * Este componente es crucial para el patrón CQRS, manteniendo sincronizadas
 * las vistas de lectura con el modelo de escritura.
 */
@Component
public class MaterializedViewUpdater {
    
    private static final Logger log = LoggerFactory.getLogger(MaterializedViewUpdater.class);
    
    private final ProjectViewRepository projectViewRepository;
    private final TaskViewRepository taskViewRepository;
    private final UserViewRepository userViewRepository;
    private final ProjectRepository projectRepository;
    
    public MaterializedViewUpdater(ProjectViewRepository projectViewRepository,
                                   TaskViewRepository taskViewRepository,
                                   UserViewRepository userViewRepository,
                                   ProjectRepository projectRepository) {
        this.projectViewRepository = projectViewRepository;
        this.taskViewRepository = taskViewRepository;
        this.userViewRepository = userViewRepository;
        this.projectRepository = projectRepository;
    }
    
    /**
     * Actualiza la vista materializada cuando se crea un proyecto.
     */
    @EventListener
    @Transactional
    @Async
    public void handleProjectCreated(ProjectCreatedEvent event) {
        log.info("Updating materialized view for project created: {}", event.getAggregateId());
        
        ProjectView view = new ProjectView(
            event.getAggregateId(),
            event.getName(),
            event.getDescription()
        );
        
        projectViewRepository.save(view);
        log.info("Project view created: {}", view.getId());
    }
    
    /**
     * Actualiza la vista materializada cuando se crea una tarea.
     */
    @EventListener
    @Transactional
    @Async
    public void handleTaskCreated(TaskCreatedEvent event) {
        log.info("Updating materialized view for task created: {}", event.getAggregateId());
        
        // Obtener nombre del proyecto
        String projectName = projectRepository.findById(event.getProjectId())
            .map(p -> p.getName())
            .orElse("Unknown");
        
        // Crear vista de tarea
        TaskView taskView = new TaskView(
            event.getAggregateId(),
            event.getTitle(),
            event.getStatus(),
            event.getProjectId(),
            projectName
        );
        taskViewRepository.save(taskView);
        
        // Actualizar contador de tareas en la vista del proyecto
        projectViewRepository.findById(event.getProjectId()).ifPresent(projectView -> {
            projectView.setTotalTasks(projectView.getTotalTasks() + 1);
            updateTaskCountsByStatus(projectView, event.getStatus(), 1);
            projectView.setLastUpdated(System.currentTimeMillis());
            projectViewRepository.save(projectView);
        });
        
        log.info("Task view created: {}", taskView.getId());
    }
    
    /**
     * Actualiza la vista materializada cuando se actualiza una tarea.
     */
    @EventListener
    @Transactional
    @Async
    public void handleTaskUpdated(TaskUpdatedEvent event) {
        log.info("Updating materialized view for task updated: {}", event.getAggregateId());
        
        taskViewRepository.findById(event.getAggregateId()).ifPresent(taskView -> {
            String oldStatus = taskView.getStatus();
            String newStatus = event.getStatus();
            Long projectId = taskView.getProjectId();
            
            // Actualizar vista de tarea
            taskView.setTitle(event.getTitle());
            taskView.setStatus(newStatus);
            taskView.setLastUpdated(System.currentTimeMillis());
            taskViewRepository.save(taskView);
            
            // Si cambió el estado, actualizar contadores del proyecto
            if (!oldStatus.equals(newStatus)) {
                projectViewRepository.findById(projectId).ifPresent(projectView -> {
                    updateTaskCountsByStatus(projectView, oldStatus, -1);
                    updateTaskCountsByStatus(projectView, newStatus, 1);
                    projectView.setLastUpdated(System.currentTimeMillis());
                    projectViewRepository.save(projectView);
                });
            }
        });
        
        log.info("Task view updated: {}", event.getAggregateId());
    }
    
    /**
     * Actualiza la vista materializada cuando se asigna un usuario a una tarea.
     */
    @EventListener
    @Transactional
    @Async
    public void handleUserAssignedToTask(UserAssignedToTaskEvent event) {
        log.info("Updating materialized view for user {} assigned to task {}", 
                event.getUserId(), event.getAggregateId());
        
        // Actualizar vista de tarea
        taskViewRepository.findById(event.getAggregateId()).ifPresent(taskView -> {
            taskView.getAssignedUserIds().add(event.getUserId());
            taskView.setAssignedUsersCount(taskView.getAssignedUserIds().size());
            taskView.setLastUpdated(System.currentTimeMillis());
            taskViewRepository.save(taskView);
        });
        
        // Actualizar vista de usuario
        userViewRepository.findById(event.getUserId()).ifPresent(userView -> {
            userView.getAssignedTaskIds().add(event.getAggregateId());
            userView.setTotalTasks(userView.getAssignedTaskIds().size());
            userView.setLastUpdated(System.currentTimeMillis());
            userViewRepository.save(userView);
        });
        
        log.info("Views updated for user-task assignment");
    }
    
    /**
     * Método auxiliar para actualizar contadores de tareas por estado.
     */
    private void updateTaskCountsByStatus(ProjectView projectView, String status, int delta) {
        switch (status.toLowerCase()) {
            case "completed":
            case "completada":
                projectView.setCompletedTasks(projectView.getCompletedTasks() + delta);
                break;
            case "in_progress":
            case "en_progreso":
                projectView.setInProgressTasks(projectView.getInProgressTasks() + delta);
                break;
            case "pending":
            case "pendiente":
                projectView.setPendingTasks(projectView.getPendingTasks() + delta);
                break;
        }
    }
}
