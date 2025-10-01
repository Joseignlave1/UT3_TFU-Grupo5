package com.example.ut3_tfu.services.implementations;

import com.example.ut3_tfu.DTOs.user.UserRequestDTO;
import com.example.ut3_tfu.DTOs.user.UserResponseDTO;

import com.example.ut3_tfu.exceptions.ResourceNotFoundException;

import com.example.ut3_tfu.models.User;

import com.example.ut3_tfu.repositories.UserRepository;

import com.example.ut3_tfu.services.interfaces.UserService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository repo;
    public UserServiceImpl(UserRepository repo) { this.repo = repo; }

    @Override
    @Transactional
    public UserResponseDTO create(UserRequestDTO dto) {
        User user = new User(dto.getUsername(), dto.getEmail());
        user = repo.save(user);
        return toResponseDTO(user);
    }

    @Override
    public Set<UserResponseDTO> findAll() {
        return repo.findAll().stream().map(this::toResponseDTO).collect(Collectors.toSet());
    }

    @Override
    public UserResponseDTO findById(Long id) {
        return repo.findById(id).map(this::toResponseDTO)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + id));
    }

    public UserResponseDTO toResponseDTO(User u) {
        UserResponseDTO r = new UserResponseDTO();
        r.setId(u.getId()); r.setUsername(u.getUsername()); r.setEmail(u.getEmail());
        return r;
    }
}
