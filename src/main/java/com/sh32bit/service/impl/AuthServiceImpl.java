package com.sh32bit.service.impl;

import com.sh32bit.dto.request.ActivateRequest;
import com.sh32bit.dto.request.LoginRequest;
import com.sh32bit.dto.response.LoginResponse;
import com.sh32bit.dto.response.MessageResponse;
import com.sh32bit.entity.User;
import com.sh32bit.exception.ConflictException;
import com.sh32bit.repository.UserRepository;
import com.sh32bit.security.AppUserDetails;
import com.sh32bit.security.AppUserDetailsService;
import com.sh32bit.service.AuthService;
import com.sh32bit.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AppUserDetailsService appUserDetailsService;

    @Override
    public MessageResponse activateUser(ActivateRequest req) {
        User user = userRepository.findByActivationToken(req.getToken())
                .orElseThrow(() -> new ConflictException("Invalid or expired token"));

        if (user.getActivationTokenCreatedAt().plusHours(24).isBefore(LocalDateTime.now())) {
            throw new ConflictException("Token expired");
        }

        user.setPassword(passwordEncoder.encode(req.getPassword()));
        user.setEnabled(true);
        user.setActivationToken(null);
        user.setActivationTokenCreatedAt(null);
        userRepository.save(user);

        return new MessageResponse("success");
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
}
