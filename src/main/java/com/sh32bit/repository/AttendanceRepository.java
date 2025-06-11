package com.sh32bit.repository;

import com.sh32bit.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    List<Attendance> findByLessonIdInAndStudentIdIn(List<Long> lessonIds, List<Long> studentIds);
}
