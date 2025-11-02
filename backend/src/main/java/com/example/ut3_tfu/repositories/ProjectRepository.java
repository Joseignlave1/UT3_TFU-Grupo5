package com.example.ut3_tfu.repositories;

import com.example.ut3_tfu.models.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> { }