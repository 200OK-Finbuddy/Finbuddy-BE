package com.http200ok.finbuddy.auth.repository;

import com.http200ok.finbuddy.auth.domain.RefreshToken;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByMemberId(String memberId);
    void deleteByMemberId(String memberId);
}
