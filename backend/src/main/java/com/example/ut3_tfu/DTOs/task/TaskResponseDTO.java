package com.example.ut3_tfu.DTOs.task;

import java.util.HashSet;
import java.util.Set;

import com.example.ut3_tfu.DTOs.user.UserResponseDTO;

public class TaskResponseDTO {
    private Long id;
    private String title;
    private String status;
    private Long projectId;
    private Set<UserResponseDTO> users = new HashSet<>();

    // getters/setters.
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Long getProjectId() { return projectId; }
    public void setProjectId(Long projectId) { this.projectId = projectId; }
    
    public Set<UserResponseDTO> getUsers() { return users; }
    public void setUsers(Set<UserResponseDTO> users) { this.users = users; }
}