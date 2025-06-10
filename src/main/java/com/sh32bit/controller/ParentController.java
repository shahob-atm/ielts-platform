package com.sh32bit.controller;

import com.sh32bit.dto.request.InviteChildRequest;
import com.sh32bit.dto.response.ApiResponse;
import com.sh32bit.dto.response.MessageResponse;
import com.sh32bit.service.ParentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/parents")
@RequiredArgsConstructor
public class ParentController {
    private final ParentService parentService;

    @PostMapping("/children/invite")
    @PreAuthorize("hasRole('PARENT')")
    public ResponseEntity<ApiResponse<MessageResponse>> inviteChild(@RequestBody @Valid InviteChildRequest req,
                                                                    Principal principal) {
        MessageResponse result = parentService.inviteChild(principal.getName(), req.getChildEmail());
        return ResponseEntity.ok(new ApiResponse<>(true, "Invitation sent to child",
                result, LocalDateTime.now()));
    }
}
