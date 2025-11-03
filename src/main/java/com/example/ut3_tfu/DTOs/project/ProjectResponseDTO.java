package com.example.ut3_tfu.DTOs.project;

import java.util.HashSet;
import java.util.Set;

import com.example.ut3_tfu.DTOs.task.TaskResponseDTO;

public class ProjectResponseDTO {
    private Long id;
    private String name;
    private String description;
    private Set<TaskResponseDTO> tasks = new HashSet<>();
    
    // getters/setters.
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public Set<TaskResponseDTO> getTasks() { return tasks; }
    public void setTasks(Set<TaskResponseDTO> tasks) { this.tasks = tasks; }
}