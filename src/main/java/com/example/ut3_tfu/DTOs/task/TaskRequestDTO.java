package com.example.ut3_tfu.DTOs.task;

public class TaskRequestDTO {
    private String title;
    private String status;
    private Long projectId;

    // getters/setters.
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public Long getProjectId() { return projectId; }
    public void setProjectId(Long projectId) { this.projectId = projectId; }
}