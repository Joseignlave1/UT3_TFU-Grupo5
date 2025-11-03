package com.example.ut3_tfu.views;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para la vista materializada de usuarios.
 */
@Repository
public interface UserViewRepository extends JpaRepository<UserView, Long> {
    
    Optional<UserView> findByUsername(String username);
    
    Optional<UserView> findByEmail(String email);
    
    @Query("SELECT u FROM UserView u WHERE u.totalTasks > 0 ORDER BY u.totalTasks DESC")
    List<UserView> findUsersWithMostTasks();
    
    @Query("SELECT u FROM UserView u WHERE ?1 MEMBER OF u.assignedTaskIds")
    List<UserView> findByAssignedTaskId(Long taskId);
}
