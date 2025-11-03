package com.example.ut3_tfu.cqrs;

import com.example.ut3_tfu.DTOs.user.UserRequestDTO;
import com.example.ut3_tfu.DTOs.user.UserResponseDTO;
import com.example.ut3_tfu.models.User;
import com.example.ut3_tfu.repositories.UserRepository;
import com.example.ut3_tfu.views.UserView;
import com.example.ut3_tfu.views.UserViewRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servicio simplificado CQRS para comandos de escritura de usuarios.
 */
@Service
@Transactional
public class UserCommandService {
    
    private final UserRepository userRepository;
    private final UserViewRepository userViewRepository;
    
    public UserCommandService(UserRepository userRepository,
                             UserViewRepository userViewRepository) {
        this.userRepository = userRepository;
        this.userViewRepository = userViewRepository;
    }
    
    public UserResponseDTO createUser(UserRequestDTO dto) {
        // Crear y guardar usuario
        User user = new User(dto.getUsername(), dto.getEmail());
        user = userRepository.save(user);
        
        // Crear vista directamente
        UserView userView = new UserView(user.getId(), user.getUsername(), user.getEmail());
        userViewRepository.save(userView);
        
        // Retornar respuesta
        UserResponseDTO response = new UserResponseDTO();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        
        return response;
    }
}
