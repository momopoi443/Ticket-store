package org.example.sbdcoursework.config;

import lombok.RequiredArgsConstructor;
import org.example.sbdcoursework.entity.user.User;
import org.example.sbdcoursework.entity.user.UserRole;
import org.example.sbdcoursework.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class BootstrapAdminUser implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        User admin = new User(UUID.randomUUID());
        admin.setRole(UserRole.ADMIN);
        admin.setEmail("admin@gmail.com");
        admin.setPassword(passwordEncoder.encode("qwerty12345"));
        admin.setFirstName("Admin");
        admin.setLastName("Admin");

        userRepository.save(admin);
    }
}
