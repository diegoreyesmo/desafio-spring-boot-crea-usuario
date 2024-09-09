package com.example.demo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.example.demo.exception.UserNotFoundException;
import com.example.demo.interceptor.LoggingInterceptor;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/v1")
class UserController {

    private final UserRepository repository;
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    UserController(UserRepository repository) {
        this.repository = repository;
    }

    // Aggregate root
    // tag::get-aggregate-root[]
    @GetMapping("/users")
    ResponseEntity<Object> all() {
        try {
            List<User> all = repository.findAll();
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
            User responseUser = repository.save(newUser);
            logger.info(String.format("saved user: %s", responseUser));
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", responseUser);
            logger.info(String.format("response: %s", response));
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("mensaje", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }

    // Single item

    @GetMapping("/users/{id}")
    ResponseEntity<Object> one(@PathVariable Long id) {
        try {
            Optional<User> byId = repository.findById(id);
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
    ResponseEntity<Object> replaceUser(@Valid @RequestBody User newUser, @PathVariable Long id) {
        try {
            Optional<User> byId = repository.findById(id);
            if (byId.isPresent()) {
                byId.map(user -> {
//                    user.setName(newUser.getName());
//                    user.setEmail(newUser.getEmail());
                    User save = repository.save(user);
                    Map<String, Object> response = new HashMap<>();
                    response.put("mensaje", save);
                    return ResponseEntity.ok(response);
                });
            } else {
                User save = repository.save(newUser);
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
            repository.deleteById(id);
            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", String.format("usuario %d eliminado correctamente", id));
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("mensaje", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }
}