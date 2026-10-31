package com.foodordering.backend.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.foodordering.backend.entity.User;
import com.foodordering.backend.entity.Role;
import com.foodordering.backend.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class InitialAdminSetup implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {

        String adminEmail = System.getenv("ADMIN_EMAIL");
        String adminPassword = System.getenv("ADMIN_PASSWORD");

        if (adminEmail == null || adminPassword == null
                || adminEmail.isBlank() || adminPassword.isBlank()) {
            System.out.println("Admin setup skipped: credentials not configured.");
            return;
        }

        if (userRepository.existsByEmail(adminEmail)) {
            System.out.println("Admin account already exists.");
            return;
        }

        User admin = new User();

        admin.setFullName("System Administrator");
        admin.setEmail(adminEmail);
        admin.setPasswordHash(passwordEncoder.encode(adminPassword));
        admin.setRole(Role.ADMIN);
        admin.setEnabled(true);

        userRepository.save(admin);

        System.out.println("Initial admin account created successfully.");
    }

}