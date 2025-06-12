package com.sh32bit.repository;

import com.sh32bit.entity.GroupStudent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface GroupStudentRepository extends JpaRepository<GroupStudent, Long> {
    @Query("""
    select gs from GroupStudent gs
    where gs.group.id = :groupId
      and gs.joinDate <= :end
      and (gs.leaveDate is null or gs.leaveDate >= :start)
""")
    List<GroupStudent> findActiveStudentsInPeriod(@Param("groupId") Long groupId,
                                                  @Param("start") LocalDate start,
                                                  @Param("end") LocalDate end);
}
