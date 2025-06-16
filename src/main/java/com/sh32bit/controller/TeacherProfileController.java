package com.sh32bit.controller;

import com.sh32bit.dto.response.ApiResponse;
import com.sh32bit.dto.response.TeacherProfileResponse;
import com.sh32bit.service.TeacherProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/teacher-profile")
@RequiredArgsConstructor
public class TeacherProfileController {
    private final TeacherProfileService teacherProfileService;

    @GetMapping("/me")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<ApiResponse<TeacherProfileResponse>> getTeacherProfile(Principal principal) {
        String email = principal.getName();
        TeacherProfileResponse result = teacherProfileService.getTeacherProfile(email);

        return ResponseEntity.ok(new ApiResponse<>(
                true,
                "success",
                result,
                LocalDateTime.now()
        ));
    }
}
