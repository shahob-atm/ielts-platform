package com.sh32bit.controller;

import com.sh32bit.dto.request.InviteChildRequest;
import com.sh32bit.dto.response.*;
import com.sh32bit.service.ParentService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

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

    @GetMapping("/me/children")
    @PreAuthorize("hasRole('PARENT')")
    public ResponseEntity<ApiResponse<List<StudentResponse>>> getChildren(Principal principal) {
        String email = principal.getName();
        List<StudentResponse> children = parentService.getChildrenOfParent(email);
        return ResponseEntity.ok(new ApiResponse<>(
                true,
                "Children found",
                children,
                LocalDateTime.now()));
    }

    @GetMapping("/{childId}/{groupId}/attendances")
    @PreAuthorize("hasRole('PARENT')")
    public ResponseEntity<ApiResponse<List<AttendanceGradeResponse>>> getAttendancesByParent(
            Principal principal,
            @PathVariable(name = "childId") @NotNull(message = "childId must be not null") Long childId,
            @PathVariable(name = "groupId") @NotNull(message = "groupId must be not null") Long groupId
    ) {
        String email = principal.getName();
        List<AttendanceGradeResponse> result = parentService.getAttendancesByParent(email, childId, groupId);

        return ResponseEntity.ok(new ApiResponse<>(
                true,
                "success",
                result,
                LocalDateTime.now()
        ));
    }

    @GetMapping("/{childId}/groups")
    @PreAuthorize("hasRole('PARENT')")
    public ResponseEntity<ApiResponse<List<GroupResponse>>> getChildGroups(
            Principal principal,
            @PathVariable(name = "childId") @NotNull(message = "ChildId must not be null") Long childId) {
        String email = principal.getName();
        List<GroupResponse> result = parentService.getChildGroups(email, childId);

        return ResponseEntity.ok(new ApiResponse<>(
                true,
                "success",
                result,
                LocalDateTime.now()
        ));
    }
}
