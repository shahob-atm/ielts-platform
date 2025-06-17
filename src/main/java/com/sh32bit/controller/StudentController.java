package com.sh32bit.controller;

import com.sh32bit.dto.response.ApiResponse;
import com.sh32bit.dto.response.AttendanceGradeResponse;
import com.sh32bit.dto.response.GroupResponse;
import com.sh32bit.dto.response.MessageResponse;
import com.sh32bit.service.StudentService;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/students")
@RequiredArgsConstructor
public class StudentController {
    private final StudentService studentService;

    @GetMapping("/parent/link")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<ApiResponse<MessageResponse>> linkParent(
            @RequestParam("token") @NotBlank(
                    message = "Token must not be blank") String token,
            Principal principal) {
        MessageResponse result = studentService.linkParent(token, principal.getName());
        return ResponseEntity.ok(new ApiResponse<>(true,
                "Parent linked successfully", result, LocalDateTime.now()));
    }

    @GetMapping("/me/groups")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<ApiResponse<List<GroupResponse>>> getMyGroups(Principal principal) {
        String email = principal.getName();
        List<GroupResponse> result = studentService.getMyGroups(email);

        return ResponseEntity.ok(new ApiResponse<>(
                true,
                "Groups found",
                result,
                LocalDateTime.now()
        ));
    }

    @GetMapping("/me/groups/{groupId}/attendances")
    public ResponseEntity<ApiResponse<List<AttendanceGradeResponse>>> getStudentAttendances(
            Principal principal,
            @PathVariable(name = "groupId") Long groupId
    ) {
        String email = principal.getName();
        List<AttendanceGradeResponse> result = studentService.getStudentAttendances(email, groupId);

        return ResponseEntity.ok(new ApiResponse<>(
                true,
                "Attendances found",
                result,
                LocalDateTime.now()
        ));
    }
}
