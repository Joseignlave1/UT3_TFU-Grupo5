package com.example.ut3_tfu.cqrs;

import com.example.ut3_tfu.views.TaskView;
import com.example.ut3_tfu.views.TaskViewRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Servicio simplificado CQRS para consultas de lectura de tareas.
 */
@Service
@Transactional(readOnly = true)
public class TaskQueryService {
    
    private final TaskViewRepository taskViewRepository;
    
    public TaskQueryService(TaskViewRepository taskViewRepository) {
        this.taskViewRepository = taskViewRepository;
    }
    
    public List<TaskView> getAllTasks() {
        return taskViewRepository.findAll();
    }
    
    public List<TaskView> getTasksByUser(Long userId) {
        return taskViewRepository.findByAssignedUserId(userId);
    }
    
    public List<TaskView> getTasksByStatus(String status) {
        return taskViewRepository.findByStatus(status);
    }
    
    public List<TaskView> getUnassignedTasks() {
        return taskViewRepository.findUnassignedTasks();
    }
}
