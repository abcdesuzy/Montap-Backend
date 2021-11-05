package com.project.montap.domain.repository;

import com.project.montap.domain.entity.ConfirmationToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, Long> {
    Optional<ConfirmationToken> findByIdxAndExpirationDateAfterAndExpired(Long idx, LocalDateTime now, int expired);
}
