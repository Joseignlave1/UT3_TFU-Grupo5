package com.example.ut3_tfu.cqrs.events;

import lombok.Getter;

/**
 * Evento publicado cuando se crea una tarea.
 */
@Getter
public class TaskCreatedEvent extends DomainEvent {
    private final String title;
    private final String status;
    private final Long projectId;
    
    public TaskCreatedEvent(Object source, Long taskId, String title, String status, Long projectId) {
        super(source, taskId);
        this.title = title;
        this.status = status;
        this.projectId = projectId;
    }
}
