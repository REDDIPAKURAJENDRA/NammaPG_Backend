package com.nammapg.config;

import com.nammapg.model.User;
import com.nammapg.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        // Seed admin user if not exists
        if (!userRepository.existsByEmail("admin@nammapg.com")) {
            User admin = User.builder()
                    .name("Admin")
                    .email("admin@nammapg.com")
                    .password(passwordEncoder.encode("admin123"))
                    .role(User.Role.ADMIN)
                    .phone("9999999999")
                    .build();
            userRepository.save(admin);
            log.info("Default admin user created: admin@nammapg.com / admin123");
        }
    }
}
