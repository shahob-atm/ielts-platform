package com.sh32bit.controller;

import com.sh32bit.dto.response.ApiResponse;
import com.sh32bit.dto.response.MessageResponse;
import com.sh32bit.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/students")
@RequiredArgsConstructor
public class StudentController {
    private final StudentService studentService;

    @GetMapping("/parent/link")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<ApiResponse<MessageResponse>> linkParent(@RequestParam String token, Principal principal) {
        MessageResponse result = studentService.linkParent(token, principal.getName());
        return ResponseEntity.ok(new ApiResponse<>(true,
                "Parent linked successfully", result, LocalDateTime.now()));
    }
}
