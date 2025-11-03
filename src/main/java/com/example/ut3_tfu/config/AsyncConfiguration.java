package com.example.ut3_tfu.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Configuración para habilitar procesamiento asíncrono.
 * Necesario para que los eventos se procesen de manera asíncrona.
 */
@Configuration
@EnableAsync
public class AsyncConfiguration {
}
