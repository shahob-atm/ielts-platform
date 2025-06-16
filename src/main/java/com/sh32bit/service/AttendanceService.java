package com.sh32bit.service;

import com.sh32bit.dto.request.AttendanceMarkRequest;
import com.sh32bit.dto.response.MessageResponse;

public interface AttendanceService {
    MessageResponse markAttendance(Long id, AttendanceMarkRequest attendanceMarkRequest);
}
