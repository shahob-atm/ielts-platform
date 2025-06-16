package com.sh32bit.service.impl;

import com.sh32bit.dto.request.AttendanceMarkRequest;
import com.sh32bit.dto.response.MessageResponse;
import com.sh32bit.entity.Attendance;
import com.sh32bit.entity.Lesson;
import com.sh32bit.entity.TeacherProfile;
import com.sh32bit.exception.ConflictException;
import com.sh32bit.exception.NotFoundException;
import com.sh32bit.repository.AttendanceRepository;
import com.sh32bit.repository.TeacherProfileRepository;
import com.sh32bit.service.AttendanceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AttendanceServiceImpl implements AttendanceService {
    private final AttendanceRepository attendanceRepository;
    private final TeacherProfileRepository teacherProfileRepository;

    @Override
    public MessageResponse markAttendance(Long id, AttendanceMarkRequest request, String email) {
        TeacherProfile teacherProfile = teacherProfileRepository.findByUserEmail(email)
                .orElseThrow(() -> new NotFoundException("teacherProfile not found"));

        Attendance attendance = attendanceRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Attendance not found"));

        Lesson lesson = attendance.getLesson();

        if (lesson == null) {
            throw new NotFoundException("Lesson not found");
        }

        if (lesson.getTeacher() == null || !lesson.getTeacher().getId().equals(teacherProfile.getId())) {
            throw new NotFoundException("Teacher not found");
        }

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
