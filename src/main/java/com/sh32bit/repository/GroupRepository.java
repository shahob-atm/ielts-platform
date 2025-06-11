package com.sh32bit.repository;

import com.sh32bit.entity.Group;
import com.sh32bit.entity.TeacherProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupRepository extends JpaRepository<Group, Long> {
    List<Group> findAllByTeacher(TeacherProfile teacher);
}
