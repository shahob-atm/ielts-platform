package com.sh32bit.repository;

import com.sh32bit.entity.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface LessonRepository extends JpaRepository<Lesson, Long> {
    @Query("""
                SELECT l FROM Lesson l
                WHERE l.groupTeacher.group.id = :groupId
                  AND l.date >= :startDate
                  AND l.date <= :endDate
                ORDER BY l.date
            """)
    List<Lesson> findByGroupIdAndDateBetween(
            @Param("groupId") Long groupId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
}
