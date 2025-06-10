package com.sh32bit.controller;

import com.sh32bit.dto.request.ActivateRequest;
import com.sh32bit.dto.request.LoginRequest;
import com.sh32bit.dto.request.ParentRegistrationRequest;
import com.sh32bit.dto.response.ApiResponse;
import com.sh32bit.dto.response.LoginResponse;
import com.sh32bit.dto.response.MessageResponse;
import com.sh32bit.service.AuthService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/activate")
    public ResponseEntity<ApiResponse<MessageResponse>> activate(@RequestBody ActivateRequest req) {
        MessageResponse result = authService.activateUser(req);

        return ResponseEntity.ok(new ApiResponse<MessageResponse>(
                true,
                "User activated successfully",
                result,
                LocalDateTime.now()
        ));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@RequestBody @Valid LoginRequest request) throws Exception {
        LoginResponse result = authService.login(request);

        return ResponseEntity.ok(new ApiResponse<LoginResponse>(
                true,
                "Successfully logged in",
                result,
                LocalDateTime.now()
        ));
    }

    @PostMapping("/register-parent")
    public ResponseEntity<ApiResponse<MessageResponse>> registerParent(@RequestBody ParentRegistrationRequest req)
            throws Exception {
        MessageResponse result = authService.registerParent(req);

        return ResponseEntity.ok(new ApiResponse<MessageResponse>(
                true,
                "Parent registered successfully",
                result,
                LocalDateTime.now()
        ));
    }

    @GetMapping("/parent-activate")
    public ResponseEntity<ApiResponse<MessageResponse>> activateParent(
            @RequestParam("token") @NotBlank(message = "Token must not be blank") String token) {
        MessageResponse result = authService.activateParent(token);

        return ResponseEntity.ok(new ApiResponse<MessageResponse>(
                true,
                "Parent activated successfully",
                result,
                LocalDateTime.now()
        ));
    }
}
