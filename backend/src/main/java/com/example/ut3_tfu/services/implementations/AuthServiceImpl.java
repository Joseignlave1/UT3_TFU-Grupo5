package com.example.ut3_tfu.services;

import com.example.ut3_tfu.DTOs.auth.UserLoginRequestDTO;
import com.example.ut3_tfu.DTOs.auth.UserRegisterRequestDTO;
import com.example.ut3_tfu.DTOs.auth.UserResponseDTO;
import com.example.ut3_tfu.exceptions.EmailAlreadyUsedException;
import com.example.ut3_tfu.exceptions.InvalidCredentialsException;
import com.example.ut3_tfu.models.User;
import com.example.ut3_tfu.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponseDTO register(UserRegisterRequestDTO dto) {
        // Validaciones de unicidad
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new EmailAlreadyUsedException("El email ya está registrado.");
        }
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new EmailAlreadyUsedException("El username ya está en uso.");
        }

        // Crear y hashear password
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        User saved = userRepository.save(user);
        return toResponse(saved);
    }

    public UserResponseDTO login(UserLoginRequestDTO dto) {
        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("Credenciales inválidas."));

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Credenciales inválidas.");
        }
        return toResponse(user);
    }

    private UserResponseDTO toResponse(User user) {
        return new UserResponseDTO(user.getId(), user.getUsername(), user.getEmail());
    }
}
