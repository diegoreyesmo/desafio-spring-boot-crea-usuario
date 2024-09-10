package com.example.demo;

import static org.mockito.Mockito.*;
import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.util.AssertionErrors.assertTrue;

import com.example.demo.model.Phone;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.ResponseEntity;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    public void testFindUserById() {
        // Preparar un mock para el objeto User
        UUID userId = UUID.randomUUID();
        User user = new User(
                userId,
                "John Doe",
                "john@example.com",
                "password",
                new ArrayList<Phone>(),
                Instant.now(),
                Instant.now(),
                Instant.now(),
                UUID.randomUUID(),
                true
        );
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        Optional<User> result = userService.findOne(userId);
        assertTrue("se espera obtener un usuario", result.isPresent());
        assertEquals("deben ser iguales", "John Doe", result.get().getName());
    }
}

