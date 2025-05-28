package com.sh32bit.service.impl;

import com.sh32bit.dto.request.InviteUserRequest;
import com.sh32bit.dto.response.MessageResponse;
import com.sh32bit.entity.StudentProfile;
import com.sh32bit.entity.TeacherProfile;
import com.sh32bit.entity.User;
import com.sh32bit.enums.Role;
import com.sh32bit.exception.EmailAlreadyExistsException;
import com.sh32bit.repository.StudentProfileRepository;
import com.sh32bit.repository.TeacherProfileRepository;
import com.sh32bit.repository.UserRepository;
import com.sh32bit.service.EmailService;
import com.sh32bit.service.UserService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final StudentProfileRepository studentProfileRepository;
    private final TeacherProfileRepository teacherProfileRepository;
    private final EmailService emailService;

    @Override
    @Transactional
    public MessageResponse inviteUser(InviteUserRequest req) throws MessagingException {
        if (userRepository.existsByEmail(req.getEmail())) {
            throw new EmailAlreadyExistsException();
        }

        String token = UUID.randomUUID().toString();

        User user = User.builder()
                .firstName(req.getFirstName())
                .lastName(req.getLastName())
                .email(req.getEmail())
                .role(req.getRole())
                .enabled(false)
                .activationToken(token)
                .activationTokenCreatedAt(LocalDateTime.now())
                .build();

        userRepository.save(user);

        if (user.getRole().equals(Role.STUDENT)) {
            StudentProfile studentProfile = StudentProfile.builder()
                    .userId(user.getId())
                    .user(user)
                    .build();

            studentProfileRepository.save(studentProfile);
        }

        if (user.getRole().equals(Role.TEACHER)) {
            TeacherProfile teacherProfile = TeacherProfile.builder()
                    .userId(user.getId())
                    .user(user)
                    .build();

            teacherProfileRepository.save(teacherProfile);
        }

        String activationLink = "http://localhost:8080/activate?token=" + token;
        emailService.send(
                req.getEmail(),
                "Activate your account",
                "<p>Hello, please activate your account: <a href=\"" + activationLink + "\">Activate</a></p>"
        );

        return new MessageResponse("success");
    }
}
