package com.smartclassroom.backend.service;

import com.smartclassroom.backend.model.User;
import com.smartclassroom.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class UserProvisioningService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public User createUser(String username, String password, String email, String role) {
        // Efficient Check
        if (userRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("Username already exists: " + username);
        }

        User user = User.builder()
                .username(username)
                .passwordHash(passwordEncoder.encode(password))
                // .email(email) // Strict schema User entity does not have email
                .role(role)
                .status("ACTIVE")
                .createdAt(LocalDateTime.now())
                .build();

        return userRepository.save(user);
    }
}
