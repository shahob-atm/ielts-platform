package com.sh32bit.controller;

import com.sh32bit.dto.request.AttendanceMarkRequest;
import com.sh32bit.dto.response.ApiResponse;
import com.sh32bit.dto.response.MessageResponse;
import com.sh32bit.service.AttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/attendance")
@RequiredArgsConstructor
public class AttendanceController {
    private final AttendanceService attendanceService;

    @PatchMapping("/{id}/mark")
    public ResponseEntity<ApiResponse<MessageResponse>> markAttendance(
            @PathVariable(name = "id") Long id,
            @RequestBody AttendanceMarkRequest attendanceMarkRequest
    ) {
        MessageResponse result = attendanceService.markAttendance(id, attendanceMarkRequest);
        return ResponseEntity.ok(new ApiResponse<>(
                true,
                "success",
                result,
                LocalDateTime.now()
        ));
    }
}
