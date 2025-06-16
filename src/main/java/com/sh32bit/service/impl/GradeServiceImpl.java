package com.sh32bit.service.impl;

import com.sh32bit.dto.request.GradeMarkRequest;
import com.sh32bit.dto.response.MessageResponse;
import com.sh32bit.entity.Attendance;
import com.sh32bit.entity.Grade;
import com.sh32bit.entity.Lesson;
import com.sh32bit.entity.TeacherProfile;
import com.sh32bit.exception.ConflictException;
import com.sh32bit.exception.NotFoundException;
import com.sh32bit.repository.AttendanceRepository;
import com.sh32bit.repository.GradeRepository;
import com.sh32bit.repository.TeacherProfileRepository;
import com.sh32bit.service.GradeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class GradeServiceImpl implements GradeService {
    private final GradeRepository gradeRepository;
    private final TeacherProfileRepository teacherProfileRepository;
    private final AttendanceRepository attendanceRepository;

    @Override
    public MessageResponse gradeMark(Long id, GradeMarkRequest request, String email) {
        TeacherProfile teacherProfile = teacherProfileRepository.findByUserEmail(email)
                .orElseThrow(() -> new NotFoundException("Teacher Profile Not Found"));

        Attendance attendance = attendanceRepository.findById(request.getAttendanceId())
                .orElseThrow(() -> new NotFoundException("Attendance Not Found"));

        Lesson lesson = attendance.getLesson();

        if (lesson == null) {
            throw new NotFoundException("Lesson Not Found");
        }

        TeacherProfile teacher = lesson.getTeacher();

        if (teacher == null) {
            throw new NotFoundException("Lesson does not have a teacher assigned");
        }

        if (!teacher.getId().equals(teacherProfile.getId())) {
            throw new ConflictException("You are not authorized to mark grade for this lesson");
        }

        if (attendance.isArchived()) {
            throw new ConflictException("Attendance is archived");
        }

        Grade grade = gradeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Grade Not Found"));

        if (grade.getAttendance() == null || !grade.getAttendance().getId().equals(attendance.getId())) {
            throw new ConflictException("Grade does not belong to the provided attendance");
        }

        grade.setScore(request.getScore());
        grade.setComment(request.getGradeComment());

        gradeRepository.save(grade);
        log.info("Teacher {} marked grade {} for student {} in lesson {}",
                email, grade.getId(), grade.getStudent().getId(), lesson.getId());

        return new MessageResponse("Successfully marked grade");
    }
}
