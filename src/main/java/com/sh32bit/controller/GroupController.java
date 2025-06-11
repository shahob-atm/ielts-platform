package com.sh32bit.controller;

import com.sh32bit.dto.response.ApiResponse;
import com.sh32bit.dto.response.GroupResponse;
import com.sh32bit.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/groups")
@RequiredArgsConstructor
public class GroupController {
    private final GroupService groupService;

    @GetMapping("/teacher-groups")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<ApiResponse<List<GroupResponse>>> getGroupsByTeacher(Principal principal) {
        String email = principal.getName();
        List<GroupResponse> groups = groupService.getGroupsByTeacher(email);

        return ResponseEntity.ok(new ApiResponse<>(
                true,
                "Groups found",
                groups,
                LocalDateTime.now())
        );
    }
}
