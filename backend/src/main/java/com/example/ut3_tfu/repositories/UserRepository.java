package com.example.ut3_tfu.repositories;

import com.example.ut3_tfu.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> { }