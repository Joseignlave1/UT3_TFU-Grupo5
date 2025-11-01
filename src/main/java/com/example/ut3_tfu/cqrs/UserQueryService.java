package com.example.ut3_tfu.cqrs;

import com.example.ut3_tfu.views.UserView;
import com.example.ut3_tfu.views.UserViewRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Servicio simplificado CQRS para consultas de lectura de usuarios.
 */
@Service
@Transactional(readOnly = true)
public class UserQueryService {
    
    private final UserViewRepository userViewRepository;
    
    public UserQueryService(UserViewRepository userViewRepository) {
        this.userViewRepository = userViewRepository;
    }
    
    public List<UserView> getAllUsers() {
        return userViewRepository.findAll();
    }
    
    public List<UserView> getUsersWithMostTasks() {
        return userViewRepository.findUsersWithMostTasks();
    }
}
