package com.sh32bit.config;

import com.sh32bit.entity.ParentProfile;
import com.sh32bit.entity.StudentProfile;
import com.sh32bit.entity.User;
import com.sh32bit.enums.Role;
import com.sh32bit.repository.ParentProfileRepository;
import com.sh32bit.repository.StudentProfileRepository;
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
    private final ParentProfileRepository parentProfileRepository;
    private final StudentProfileRepository studentProfileRepository;
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
                        .build(),
                User.builder()
                        .firstName("Dilshod")
                        .lastName("Jo'rayev")
                        .email("dilshod@gmail.com")
                        .role(Role.PARENT)
                        .enabled(true)
                        .password(passwordEncoder.encode("123456"))
                        .build(),
                User.builder()
                        .firstName("Ali")
                        .lastName("Jo'rayev")
                        .email("ali@gmail.com")
                        .role(Role.STUDENT)
                        .enabled(true)
                        .password(passwordEncoder.encode("123456"))
                        .build()
        );

        userRepository.saveAll(userList);

        StudentProfile studentProfile = StudentProfile.builder()
                .user(userList.get(2))
                .build();

        studentProfileRepository.save(studentProfile);

        ParentProfile parentProfile = ParentProfile.builder()
                .user(userList.get(1))
                .build();

        parentProfileRepository.save(parentProfile);
    }
}
