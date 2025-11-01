package com.example.ut3_tfu.cqrs.events;

import lombok.Getter;

/**
 * Evento publicado cuando se crea un proyecto.
 */
@Getter
public class ProjectCreatedEvent extends DomainEvent {
    private final String name;
    private final String description;
    
    public ProjectCreatedEvent(Object source, Long projectId, String name, String description) {
        super(source, projectId);
        this.name = name;
        this.description = description;
    }
}
