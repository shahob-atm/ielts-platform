package com.sh32bit.repository;

import com.sh32bit.entity.LinkToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LinkTokenRepository extends JpaRepository<LinkToken, Long> {
    Optional<LinkToken> findByToken(String token);
}
