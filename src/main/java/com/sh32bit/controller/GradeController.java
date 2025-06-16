package com.sh32bit.controller;

import com.sh32bit.dto.request.GradeMarkRequest;
import com.sh32bit.dto.response.ApiResponse;
import com.sh32bit.dto.response.MessageResponse;
import com.sh32bit.service.GradeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/grade")
@RequiredArgsConstructor
public class GradeController {
    private final GradeService gradeService;

    @PatchMapping("/{id}/mark")
    public ResponseEntity<ApiResponse<MessageResponse>> gradeMark(
            @PathVariable(name = "id") Long id,
            @Valid @RequestBody GradeMarkRequest request,
            Principal principal
    ) {
        String email = principal.getName();
        MessageResponse result = gradeService.gradeMark(id, request, email);

        return ResponseEntity.ok(new ApiResponse<>(
                true,
                "success",
                result,
                LocalDateTime.now()
        ));
    }
}
