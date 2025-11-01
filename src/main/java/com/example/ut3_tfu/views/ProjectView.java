package com.example.ut3_tfu.views;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Vista materializada para proyectos con estadísticas precalculadas.
 * Esta vista se actualiza mediante eventos para optimizar las consultas.
 */
@Entity
@Table(name = "project_view")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectView {
    
    @Id
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "total_tasks")
    private Integer totalTasks = 0;
    
    @Column(name = "completed_tasks")
    private Integer completedTasks = 0;
    
    @Column(name = "in_progress_tasks")
    private Integer inProgressTasks = 0;
    
    @Column(name = "pending_tasks")
    private Integer pendingTasks = 0;
    
    @Column(name = "last_updated")
    private Long lastUpdated;
    
    public ProjectView(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.totalTasks = 0;
        this.completedTasks = 0;
        this.inProgressTasks = 0;
        this.pendingTasks = 0;
        this.lastUpdated = System.currentTimeMillis();
    }
}
