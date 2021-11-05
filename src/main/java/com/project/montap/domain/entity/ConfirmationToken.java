package com.project.montap.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class ConfirmationToken {

    private static final long EMAIL_TOKEN_EXPIRATION_TIME_VALUE = 5L; //토큰 만료 시간

    @Id
    @GeneratedValue
    @Column( length = 36 )
    private Long idx;

    @Column // 완료 시간
    private LocalDateTime expirationDate;

    @Column( columnDefinition = "INTEGER NOT NULL DEFAULT 0" ) // 토큰 유효기간 만료 여부 : 0(활성), 1(만료)
    private int expired = 0;

    private String userId;

    // 이메일 인증 토큰 생성
    public static ConfirmationToken createEmailConfirmationToken(String userId) {
        ConfirmationToken confirmationToken = new ConfirmationToken();
        confirmationToken.expirationDate = LocalDateTime.now().plusMinutes(EMAIL_TOKEN_EXPIRATION_TIME_VALUE); // 5분 후 만료
        confirmationToken.userId = userId;
        return confirmationToken;
    }

    /**
     * 토큰 사용으로 인한 만료
     */
    public void useToken() {
        expired = 1;
    }

}
