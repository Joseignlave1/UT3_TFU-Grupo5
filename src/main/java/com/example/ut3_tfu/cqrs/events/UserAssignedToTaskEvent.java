package com.example.ut3_tfu.cqrs.events;

import lombok.Getter;

/**
 * Evento publicado cuando se asigna un usuario a una tarea.
 */
@Getter
public class UserAssignedToTaskEvent extends DomainEvent {
    private final Long userId;
    
    public UserAssignedToTaskEvent(Object source, Long taskId, Long userId) {
        super(source, taskId);
        this.userId = userId;
    }
}
