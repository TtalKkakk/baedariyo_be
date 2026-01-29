package com.house.biet.auth.command.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken {

    @Id
    @GeneratedValue
    private Long id;

    private Long accountId;

    @Column(nullable = false, unique = true)
    private String token;

    private LocalDateTime expiredAt;

    private RefreshToken(Long accountId, String token, LocalDateTime expiredAt) {
        this.accountId = accountId;
        this.token = token;
        this.expiredAt = expiredAt;
    }

    public static RefreshToken issue(Long accountId, String token, LocalDateTime expiredAt) {
        return new RefreshToken(accountId, token, expiredAt);
    }

    public boolean isExpired() {
        return expiredAt.isBefore(LocalDateTime.now());
    }
}
