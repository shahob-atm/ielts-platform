package com.sh32bit.repository;

import com.sh32bit.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    List<Attendance> findByLessonIdInAndStudentIdIn(List<Long> lessonIds, List<Long> studentIds);

    @Query("SELECT a FROM Attendance a WHERE a.lesson.date < :nowDate AND a.archived = false")
    List<Attendance> findOldAttendances(@Param("nowDate") LocalDate nowDate);
}
