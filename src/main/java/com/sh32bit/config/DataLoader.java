package com.sh32bit.config;

import com.sh32bit.entity.User;
import com.sh32bit.enums.Role;
import com.sh32bit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        List<User> userList = List.of(
                User.builder()
                        .firstName("John")
                        .lastName("Doe")
                        .email("johndoe@gmail.com")
                        .role(Role.ADMIN)
                        .enabled(true)
                        .password(passwordEncoder.encode("123456"))
                        .build()
        );

        userRepository.saveAll(userList);
    }
}
