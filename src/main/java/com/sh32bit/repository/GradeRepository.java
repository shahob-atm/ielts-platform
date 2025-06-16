package com.sh32bit.repository;

import com.sh32bit.entity.Grade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GradeRepository extends JpaRepository<Grade, Long> {
    List<Grade> findByAttendance_Lesson_IdInAndStudent_IdIn(List<Long> lessonIds, List<Long> studentIds);
}
