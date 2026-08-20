package com.taskmanagement.aitaskmanagement.config;

import com.taskmanagement.aitaskmanagement.entity.Role;
import com.taskmanagement.aitaskmanagement.entity.User;
import com.taskmanagement.aitaskmanagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitilizer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {

        String adminEmail = "admin1@workforge.com";
        if(userRepository.existsByEmail(adminEmail)) {
            return;
        }
        User admin = User.builder()
                .name("Admin1")
                .email(adminEmail)
                .password(passwordEncoder.encode("admin1@123"))
                .role(Role.Admin)
                .build();
        userRepository.save(admin);

        System.out.println("Admin is created with email:"+adminEmail);

    }
}
