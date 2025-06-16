package com.sh32bit.repository;

import com.sh32bit.entity.GroupTeacher;
import com.sh32bit.entity.TeacherProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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

    @Query("""
                select gt from GroupTeacher gt
                where gt.group.id = :groupId
                  and gt.teacher.id = :teacherId
                  and gt.joinDate <= :today
                  and (gt.leaveDate is null or gt.leaveDate >= :today)
            """)
    Optional<GroupTeacher> findActiveGroupTeacher(
            @Param("groupId") Long groupId,
            @Param("teacherId") Long teacherId,
            @Param("today") LocalDate today
    );

    @Query("SELECT gt.teacher FROM GroupTeacher gt " +
            "WHERE gt.group.id = :groupId AND gt.leaveDate is null")
    Optional<TeacherProfile> findActiveTeacherByGroupId(@Param("groupId") Long groupId);
}
