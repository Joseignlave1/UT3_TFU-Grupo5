package com.example.ut3_tfu.views;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

/**
 * Vista materializada para usuarios con estadísticas de tareas.
 */
@Entity
@Table(name = "user_view")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserView {
    
    @Id
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String username;
    
    @Column(nullable = false, unique = true)
    private String email;
    
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_view_assigned_tasks", 
                     joinColumns = @JoinColumn(name = "user_view_id"))
    @Column(name = "task_id")
    private Set<Long> assignedTaskIds = new HashSet<>();
    
    @Column(name = "total_tasks")
    private Integer totalTasks = 0;
    
    @Column(name = "completed_tasks")
    private Integer completedTasks = 0;
    
    @Column(name = "last_updated")
    private Long lastUpdated;
    
    public UserView(Long id, String username, String email) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.totalTasks = 0;
        this.completedTasks = 0;
        this.lastUpdated = System.currentTimeMillis();
    }
}
