package com.example.demo.service;

import com.example.demo.model.User;
import com.example.demo.repository.PhoneRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.util.UuidGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PhoneRepository phoneRepository;

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    public UserService(UserRepository userRepository, PhoneRepository phoneRepository) {
        this.userRepository = userRepository;
        this.phoneRepository = phoneRepository;
    }

    public ResponseEntity<Object> all() {
        List<User> all = userRepository.findAll();
        Map<String, Object> response = new HashMap<>();
        response.put("mensaje", all);
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<Object> newUser(User newUser) {
        Optional<User> byEmail = userRepository.findByEmail(newUser.getEmail());
        if (byEmail.isPresent()) {
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "El correo ya está registrado");
            logger.info(String.format("response: %s", response));
            return new ResponseEntity<>(response, HttpStatus.CONFLICT);
        } else {
            newUser.getPhones().forEach(phoneRepository::save);
            newUser.setCreated(Instant.now());
            newUser.setLast_login(Instant.now());
            newUser.setIsactive(true);
            newUser.setToken(UuidGenerator.generateUuid());
            User responseUser = userRepository.save(newUser);
            logger.info(String.format("saved user: %s", responseUser));
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", responseUser);
            logger.info(String.format("response: %s", response));
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        }
    }

    public Optional<User> findOne(UUID id) {
        return userRepository.findById(id);

    }

    public ResponseEntity<Object> replaceUser(User newUser, UUID id) {
        Optional<User> byId = userRepository.findById(id);
        if (byId.isPresent()) {
            byId.map(user -> {
                boolean modificado = false;
                if (newUser.getName() != null) {
                    user.setName(newUser.getName());
                    modificado = true;
                }
                if (newUser.getEmail() != null) {
                    user.setEmail(newUser.getEmail());
                    modificado = true;
                }
                if (newUser.getPassword() != null) {
                    user.setPassword(newUser.getPassword());
                    modificado = true;
                }
                if (newUser.getLast_login() != null) {
                    user.setLast_login(newUser.getLast_login());
                    modificado = true;
                }
                if (newUser.getToken() != null) {
                    user.setToken(newUser.getToken());
                    modificado = true;
                }
                if (newUser.getIsactive() != null) {
                    user.setIsactive(newUser.getIsactive());
                    modificado = true;
                }
                if (modificado) {
                    user.setModified(Instant.now());
                    User save = userRepository.save(user);
                    Map<String, Object> response = new HashMap<>();
                    response.put("mensaje", user);
                    return ResponseEntity.ok(response);
                }
                return ResponseEntity.badRequest().build();
            });
        } else {
            return newUser(newUser);
        }
        return ResponseEntity.badRequest().build();
    }

    public ResponseEntity<Object> deleteById(UUID id) {
        userRepository.deleteById(id);
        Map<String, Object> response = new HashMap<>();
        response.put("mensaje", String.format("usuario %s eliminado correctamente", id));
        return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
    }
}
