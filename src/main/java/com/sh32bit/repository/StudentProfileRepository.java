package com.sh32bit.repository;

import com.sh32bit.entity.StudentProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentProfileRepository extends JpaRepository<StudentProfile, Long> {
    Optional<StudentProfile> findByUserEmail(String email);

    Optional<StudentProfile> findByUserId(Long id);
}
