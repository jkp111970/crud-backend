package com.example.crud_backend.config;

import com.example.crud_backend.entity.Role;
import com.example.crud_backend.entity.User;
import com.example.crud_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Override
    public void run(String... args) throws Exception {
        // Create default users if they don't exist
        if (!userRepository.existsByUsername("admin")) {
            User admin = new User(
                    "admin",
                    "admin@example.com",
                    passwordEncoder.encode("admin123"),
                    Role.ADMIN
            );
            userRepository.save(admin);
            System.out.println("Default admin user created: admin/admin123");
        }
        
        if (!userRepository.existsByUsername("user")) {
            User user = new User(
                    "user",
                    "user@example.com",
                    passwordEncoder.encode("user123"),
                    Role.USER
            );
            userRepository.save(user);
            System.out.println("Default user created: user/user123");
        }
        
        if (!userRepository.existsByUsername("moderator")) {
            User moderator = new User(
                    "moderator",
                    "moderator@example.com",
                    passwordEncoder.encode("moderator123"),
                    Role.MODERATOR
            );
            userRepository.save(moderator);
            System.out.println("Default moderator user created: moderator/moderator123");
        }
    }
} 