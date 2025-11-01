package com.example.ut3_tfu.views;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

/**
 * Vista materializada para tareas con información desnormalizada.
 * Optimiza las consultas evitando múltiples joins.
 */
@Entity
@Table(name = "task_view")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskView {
    
    @Id
    private Long id;
    
    @Column(nullable = false)
    private String title;
    
    @Column(nullable = false)
    private String status;
    
    @Column(name = "project_id")
    private Long projectId;
    
    @Column(name = "project_name")
    private String projectName;
    
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "task_view_assigned_users", 
                     joinColumns = @JoinColumn(name = "task_view_id"))
    @Column(name = "user_id")
    private Set<Long> assignedUserIds = new HashSet<>();
    
    @Column(name = "assigned_users_count")
    private Integer assignedUsersCount = 0;
    
    @Column(name = "last_updated")
    private Long lastUpdated;
    
    public TaskView(Long id, String title, String status, Long projectId, String projectName) {
        this.id = id;
        this.title = title;
        this.status = status;
        this.projectId = projectId;
        this.projectName = projectName;
        this.assignedUsersCount = 0;
        this.lastUpdated = System.currentTimeMillis();
    }
}
