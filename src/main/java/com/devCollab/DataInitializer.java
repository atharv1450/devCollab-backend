package com.devCollab;

import com.devCollab.entity.Role;
import com.devCollab.entity.User;
import com.devCollab.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (!userRepository.existsByEmail("john@test.com")) {
            userRepository.save(User.builder()
                    .username("john")
                    .email("john@test.com")
                    .password(passwordEncoder.encode("secret123"))
                    .role(Role.ROLE_DEVELOPER)
                    .build());
        }
        if (!userRepository.existsByEmail("jane@test.com")) {
            userRepository.save(User.builder()
                    .username("jane")
                    .email("jane@test.com")
                    .password(passwordEncoder.encode("secret123"))
                    .role(Role.ROLE_DEVELOPER)
                    .build());
        }
        if (!userRepository.existsByEmail("admin@test.com")) {
            userRepository.save(User.builder()
                    .username("admin")
                    .email("admin@test.com")
                    .password(passwordEncoder.encode("admin123"))
                    .role(Role.ROLE_ADMIN)
                    .build());
        }
    }
}