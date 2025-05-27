package com.sh32bit.controller;

import com.sh32bit.dto.request.InviteUserRequest;
import com.sh32bit.dto.response.MessageResponse;
import com.sh32bit.service.UserService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/invite")
    public ResponseEntity<MessageResponse> inviteUser(@RequestBody InviteUserRequest req)
            throws MessagingException {
        return ResponseEntity.ok(userService.inviteUser(req));
    }
}
