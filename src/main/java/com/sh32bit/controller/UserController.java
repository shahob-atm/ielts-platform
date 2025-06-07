package com.sh32bit.controller;

import com.sh32bit.dto.request.InviteUserRequest;
import com.sh32bit.dto.response.ApiResponse;
import com.sh32bit.dto.response.MessageResponse;
import com.sh32bit.service.UserService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/invite")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ApiResponse<MessageResponse>> inviteUser(@RequestBody InviteUserRequest req)
            throws MessagingException {
        MessageResponse result = userService.inviteUser(req);

        return ResponseEntity.ok(new ApiResponse<MessageResponse>(
                true,
                "User Invited Successfully",
                result,
                LocalDateTime.now()
        ));
    }
}
