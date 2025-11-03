package com.example.ut3_tfu.views;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio para la vista materializada de tareas.
 */
@Repository
public interface TaskViewRepository extends JpaRepository<TaskView, Long> {
    
    List<TaskView> findByProjectId(Long projectId);
    
    List<TaskView> findByStatus(String status);
    
    @Query("SELECT t FROM TaskView t WHERE ?1 MEMBER OF t.assignedUserIds")
    List<TaskView> findByAssignedUserId(Long userId);
    
    @Query("SELECT t FROM TaskView t WHERE t.assignedUsersCount = 0")
    List<TaskView> findUnassignedTasks();
}
