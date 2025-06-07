package com.sh32bit.service.impl;

import com.sh32bit.dto.request.InviteUserRequest;
import com.sh32bit.dto.response.MessageResponse;
import com.sh32bit.entity.StudentProfile;
import com.sh32bit.entity.TeacherProfile;
import com.sh32bit.entity.User;
import com.sh32bit.enums.Role;
import com.sh32bit.event.UserInvitedEvent;
import com.sh32bit.exception.EmailAlreadyExistsException;
import com.sh32bit.mapper.UserMapper;
import com.sh32bit.repository.StudentProfileRepository;
import com.sh32bit.repository.TeacherProfileRepository;
import com.sh32bit.repository.UserRepository;
import com.sh32bit.service.UserService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final StudentProfileRepository studentProfileRepository;
    private final TeacherProfileRepository teacherProfileRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    @Transactional
    public MessageResponse inviteUser(InviteUserRequest req) throws MessagingException {
        if (userRepository.existsByEmail(req.getEmail())) {
            throw new EmailAlreadyExistsException();
        }

        String token = UUID.randomUUID().toString();

        User user = UserMapper.toEntity(req);

        user.setActivationToken(token);
        user.setActivationTokenCreatedAt(LocalDateTime.now());

        userRepository.save(user);

        if (user.getRole().equals(Role.STUDENT)) {
            StudentProfile studentProfile = StudentProfile.builder()
                    .user(user)
                    .build();

            studentProfileRepository.save(studentProfile);
        }

        if (user.getRole().equals(Role.TEACHER)) {
            TeacherProfile teacherProfile = TeacherProfile.builder()
                    .user(user)
                    .build();

            teacherProfileRepository.save(teacherProfile);
        }

        String activationLink = "http://localhost:8080/activate?token=" + token;
        eventPublisher.publishEvent(new UserInvitedEvent(this, user, activationLink));

        log.info("User invited successfully: {}", user.getEmail());

        return new MessageResponse("User Invited Successfully");
    }
}
