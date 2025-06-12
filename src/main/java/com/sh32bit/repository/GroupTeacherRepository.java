package com.sh32bit.repository;

import com.sh32bit.entity.GroupTeacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface GroupTeacherRepository extends JpaRepository<GroupTeacher, Long> {
    @Query("""
    select gt from GroupTeacher gt
    where gt.teacher.id = :teacherId
      and gt.joinDate <= :now
      and (gt.leaveDate is null or gt.leaveDate >= :now)
""")
    List<GroupTeacher> findActiveGroupsForTeacher(
            @Param("teacherId") Long teacherId,
            @Param("now") LocalDate now
    );
}
