package com.example.ut3_tfu.views;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio para la vista materializada de proyectos.
 * Solo operaciones de lectura y actualizaciones desde eventos.
 */
@Repository
public interface ProjectViewRepository extends JpaRepository<ProjectView, Long> {
    
    @Query("SELECT p FROM ProjectView p WHERE p.totalTasks > 0 ORDER BY p.lastUpdated DESC")
    List<ProjectView> findAllWithTasks();
    
    @Query("SELECT p FROM ProjectView p WHERE p.completedTasks = p.totalTasks AND p.totalTasks > 0")
    List<ProjectView> findCompletedProjects();
    
    @Query("SELECT p FROM ProjectView p WHERE p.name LIKE %?1%")
    List<ProjectView> searchByName(String name);
}
