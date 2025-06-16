package com.sh32bit.service.impl;

import com.sh32bit.dto.request.AttendanceMarkRequest;
import com.sh32bit.dto.response.MessageResponse;
import com.sh32bit.entity.Attendance;
import com.sh32bit.exception.ConflictException;
import com.sh32bit.exception.NotFoundException;
import com.sh32bit.repository.AttendanceRepository;
import com.sh32bit.service.AttendanceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AttendanceServiceImpl implements AttendanceService {
    private final AttendanceRepository attendanceRepository;

    @Override
    public MessageResponse markAttendance(Long id, AttendanceMarkRequest request) {
        Attendance attendance = attendanceRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Attendance not found"));

        if (attendance.isArchived()) {
            throw new ConflictException("Attendance is archived");
        }

        attendance.setPresent(request.isPresent());
        attendance.setComment(request.getComment());

        attendanceRepository.save(attendance);
        log.info("attendance marked successfully {}", attendance.toString());

        return new MessageResponse("Attendance marked successfully");
    }
}
