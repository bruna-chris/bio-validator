package com.bruna.biometria.service;

import com.bruna.biometria.domain.model.User;
import com.bruna.biometria.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final Random random = new Random();

    public User createUser(User user) {
        // Se o documento já existir, lança um 409 (Conflict) direto
        if (userRepository.existsByDocument(user.getDocument())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Documento já cadastrado.");
        }
        return userRepository.save(user);
    }

    public List<User> listAllUsers() {
        return userRepository.findAll();
    }

    public User findUserById(UUID id) {
        // Se não achar o ID, lança um 404 (Not Found) direto
        return userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado."));
    }

    public User validateBiometrics(UUID id) {
        User user = findUserById(id);
    
        if (!"PENDING".equals(user.getStatus())) {
            return user;
        }
    
        // Volta a ser puramente a regra original dos 80% / 20%
        String novoStatus = (random.nextInt(100) < 80) ? "APPROVED" : "REJECTED";
        user.setStatus(novoStatus);
    
        return userRepository.save(user);
    }
       
}