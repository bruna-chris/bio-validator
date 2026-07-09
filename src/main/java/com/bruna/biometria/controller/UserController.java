package com.bruna.biometria.controller;

import com.bruna.biometria.controller.dto.UserDTO;
import com.bruna.biometria.domain.model.User;
import com.bruna.biometria.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    //Criar Usuário (Retorna 201 Created)
    @PostMapping
    public ResponseEntity<User> createUser(@Valid @RequestBody UserDTO request) {
        User user = User.builder()
                .name(request.getName())
                .document(request.getDocument())
                .build();
        
        User savedUser = userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }

    //Listar Todos os Usuários (Retorna 200 OK)
    @GetMapping
    public ResponseEntity<List<User>> listAll() {
        return ResponseEntity.ok(userService.listAllUsers());
    }

    //Buscar Usuário por ID (Retorna 200 OK ou 404 se não achar)
    @GetMapping("/{id}")
    public ResponseEntity<User> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(userService.findUserById(id));
    }

    //Validar Biometria (Retorna 200 OK com o resultado APPROVED/REJECTED)
    @PostMapping("/{id}/validate")
    public ResponseEntity<User> validateBiometrics(@PathVariable UUID id) {
        return ResponseEntity.ok(userService.validateBiometrics(id));
    }
}
