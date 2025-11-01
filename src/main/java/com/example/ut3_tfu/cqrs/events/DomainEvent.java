package com.example.ut3_tfu.cqrs.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * Clase base para todos los eventos del dominio.
 */
@Getter
public abstract class DomainEvent extends ApplicationEvent {
    private final Long aggregateId;
    private final Long eventTimestamp;
    
    public DomainEvent(Object source, Long aggregateId) {
        super(source);
        this.aggregateId = aggregateId;
        this.eventTimestamp = System.currentTimeMillis();
    }
}
