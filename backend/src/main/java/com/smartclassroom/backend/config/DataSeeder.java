package com.smartclassroom.backend.config;

import com.smartclassroom.backend.model.User;
import com.smartclassroom.backend.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.findByUsername("admin").isEmpty()) {
            User admin = User.builder()
                    .username("admin")
                    .passwordHash(passwordEncoder.encode("admin123"))
                    .role("ADMIN")
                    .status("ACTIVE")
                    .createdAt(LocalDateTime.now())
                    .build();

            userRepository.save(admin);
            System.out.println("Admin user seeded successfully: admin / admin123");
        } else {
            System.out.println("Admin user already exists.");
        }
    }
}
