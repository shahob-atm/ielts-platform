package com.sh32bit.controller;

import com.sh32bit.dto.request.ActivateRequest;
import com.sh32bit.dto.request.LoginRequest;
import com.sh32bit.dto.response.LoginResponse;
import com.sh32bit.dto.response.MessageResponse;
import com.sh32bit.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/activate")
    public ResponseEntity<MessageResponse> activate(@RequestBody ActivateRequest req) {
        return ResponseEntity.ok(authService.activateUser(req));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) throws Exception {
        return ResponseEntity.ok(authService.login(request));
    }
}
