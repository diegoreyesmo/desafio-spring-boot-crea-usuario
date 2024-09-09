package com.example.demo.controller;

import java.time.Instant;
import java.util.*;

import com.example.demo.exception.UserNotFoundException;
import com.example.demo.model.User;
import com.example.demo.repository.PhoneRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.util.UuidGenerator;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/v1")
class UserController {

    private final UserRepository userRepository;
    private final PhoneRepository phoneRepository;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    UserController(UserRepository userRepository, PhoneRepository phoneRepository) {
        this.userRepository = userRepository;
        this.phoneRepository = phoneRepository;
    }

    // Aggregate root
    // tag::get-aggregate-root[]
    @GetMapping("/users")
    ResponseEntity<Object> all() {
        try {
            List<User> all = userRepository.findAll();
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", all);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("mensaje", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }


    }
    // end::get-aggregate-root[]

    @PostMapping(value = "/users", consumes = "application/json")
    ResponseEntity<Object> newUser(@Valid @RequestBody User newUser) {
        try {
            logger.info(String.format("request: %s", newUser.toString()));
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
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("mensaje", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }

    // Single item

    @GetMapping("/users/{id}")
    ResponseEntity<Object> one(@PathVariable UUID id) {
        try {
            Optional<User> byId = userRepository.findById(id);
            if (byId.isPresent()) {
                Map<String, Object> response = new HashMap<>();
                response.put("mensaje", byId.get());
                return ResponseEntity.ok(response);
            } else {
                throw new UserNotFoundException(id);
            }
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("mensaje", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

    }

    @PutMapping("/users/{id}")
    ResponseEntity<Object> replaceUser(@Valid @RequestBody User newUser, @PathVariable UUID id) {
        try {
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
                    if(modificado){
                        user.setModified(Instant.now());
                    }
                    User save = userRepository.save(user);
                    Map<String, Object> response = new HashMap<>();
                    response.put("mensaje", user);
                    return ResponseEntity.ok(response);
                });
            } else {
                User save = userRepository.save(newUser);
                Map<String, Object> response = new HashMap<>();
                response.put("mensaje", save);
                return ResponseEntity.ok(response);
            }
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("mensaje", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
        return null;
    }

    @DeleteMapping("/users/{id}")
    ResponseEntity<Object> deleteUser(@PathVariable Long id) throws Exception {
        try {
            userRepository.deleteById(id);
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", String.format("usuario %d eliminado correctamente", id));
            return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("mensaje", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }
}