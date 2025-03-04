package com.http200ok.finbuddy.auth.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Entity
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "token_id")
    private Long id;

    @Column(nullable = false, unique = true, name = "member_id")
    private String memberId;

    @Column(nullable = false)
    private String token; // Refresh Token

    public RefreshToken(String memberId, String token) {
        this.memberId = memberId;
        this.token = token;
    }

    public void updateToken(String newToken) {
        this.token = newToken;
    }

}
