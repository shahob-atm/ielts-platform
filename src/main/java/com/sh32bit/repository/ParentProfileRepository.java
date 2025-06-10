package com.sh32bit.repository;

import com.sh32bit.entity.ParentProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ParentProfileRepository extends JpaRepository<ParentProfile, Long> {
    Optional<ParentProfile> findByUserId(Long user_id);
}
