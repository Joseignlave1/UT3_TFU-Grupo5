package com.example.ut3_tfu.services.interfaces;

import com.example.ut3_tfu.DTOs.user.UserRequestDTO;
import com.example.ut3_tfu.DTOs.user.UserResponseDTO;

import java.util.Set;

public interface UserService {
    UserResponseDTO create(UserRequestDTO dto);

    Set<UserResponseDTO> findAll();
    
    UserResponseDTO findById(Long id);
}