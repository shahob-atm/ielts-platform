package com.sh32bit.service.impl;

import com.sh32bit.dto.request.ActivateRequest;
import com.sh32bit.dto.request.LoginRequest;
import com.sh32bit.dto.request.ParentRegistrationRequest;
import com.sh32bit.dto.response.LoginResponse;
import com.sh32bit.dto.response.MessageResponse;
import com.sh32bit.entity.ParentProfile;
import com.sh32bit.entity.User;
import com.sh32bit.enums.Role;
import com.sh32bit.event.ParentRegisteredEvent;
import com.sh32bit.exception.ConflictException;
import com.sh32bit.exception.EmailAlreadyExistsException;
import com.sh32bit.mapper.ParentMapper;
import com.sh32bit.repository.ParentProfileRepository;
import com.sh32bit.repository.UserRepository;
import com.sh32bit.security.AppUserDetails;
import com.sh32bit.service.AuthService;
import com.sh32bit.util.JwtUtil;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final ParentProfileRepository parentProfileRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    public MessageResponse activateUser(ActivateRequest req) {
        User user = userRepository.findByActivationToken(req.getToken())
                .orElseThrow(() -> new ConflictException("Invalid token"));

        if (user.getActivationTokenCreatedAt().plusHours(24).isBefore(LocalDateTime.now())) {
            throw new ConflictException("Token expired");
        }

        user.setPassword(passwordEncoder.encode(req.getPassword()));
        user.setEnabled(true);
        user.setActivationToken(null);
        user.setActivationTokenCreatedAt(null);
        userRepository.save(user);

        log.info("User activated successfully {}", user.getEmail());
        return new MessageResponse("User activated successfully");
    }

    @Override
    public LoginResponse login(LoginRequest request) throws Exception {
        log.info("Login requested for username: {}", request.getEmail());

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        AppUserDetails userDetails = (AppUserDetails) authentication.getPrincipal();

        String accessToken = jwtUtil.generateAccessToken(userDetails);
        String refreshToken = jwtUtil.generateRefreshToken(userDetails);

        log.info("Login successful: userId={}, role={}",
                userDetails.user().getId(), userDetails.user().getRole());

        return new LoginResponse(accessToken, refreshToken);
    }

    @Override
    @Transactional
    public MessageResponse registerParent(ParentRegistrationRequest req) throws MessagingException {
        if (userRepository.existsByEmail(req.getEmail())) {
            throw new EmailAlreadyExistsException();
        }

        String activationCode = UUID.randomUUID().toString();

        User user = ParentMapper.toEntity(req);

        user.setPassword(passwordEncoder.encode(req.getPassword()));
        user.setActivationToken(activationCode);
        user.setActivationTokenCreatedAt(LocalDateTime.now());

        userRepository.save(user);

        ParentProfile parentProfile = ParentProfile.builder()
                .user(user)
                .build();

        parentProfileRepository.save(parentProfile);
        log.info("Parent registered successfully: {}", user.getEmail());

        String activationLink = "https://yourdomain.com/parent-activate?token=" + activationCode;
        eventPublisher.publishEvent(new ParentRegisteredEvent(this, user, activationLink));

        return new MessageResponse("Parent registered successfully");
    }

    @Override
    public MessageResponse activateParent(String token) {
        User user = userRepository.findByActivationToken(token)
                .orElseThrow(() -> new ConflictException("Invalid token"));

        if (!user.getRole().equals(Role.PARENT)) {
            throw new ConflictException("Invalid token");
        }

        if (user.getActivationTokenCreatedAt().plusHours(24).isBefore(LocalDateTime.now())) {
            throw new ConflictException("Token expired");
        }

        user.setActivationToken(null);
        user.setActivationTokenCreatedAt(null);
        user.setEnabled(true);

        userRepository.save(user);
        log.info("Parent activated successfully {}", user.getEmail());

        return new MessageResponse("Parent activated successfully");
    }
}
