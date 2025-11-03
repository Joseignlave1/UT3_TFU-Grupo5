package com.example.ut3_tfu.cqrs.events;

import lombok.Getter;

/**
 * Evento publicado cuando se actualiza una tarea.
 */
@Getter
public class TaskUpdatedEvent extends DomainEvent {
    private final String title;
    private final String status;
    
    public TaskUpdatedEvent(Object source, Long taskId, String title, String status) {
        super(source, taskId);
        this.title = title;
        this.status = status;
    }
}
