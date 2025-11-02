package com.example.ut3_tfu.repositories;

import com.example.ut3_tfu.models.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> { }