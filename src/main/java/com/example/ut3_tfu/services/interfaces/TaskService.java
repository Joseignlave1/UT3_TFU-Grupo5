package com.example.ut3_tfu.services.interfaces;

import com.example.ut3_tfu.DTOs.task.TaskRequestDTO;
import com.example.ut3_tfu.DTOs.task.TaskResponseDTO;

public interface TaskService {
    TaskResponseDTO create(TaskRequestDTO dto);

    TaskResponseDTO findById(Long id);
    
    void assignUser(Long taskId, Long userId);
}