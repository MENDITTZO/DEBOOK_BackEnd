package com.mendittzo.user.command.domain.aggregate;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "user")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EntityListeners(AuditingEntityListener.class)
@SQLDelete(sql = "UPDATE user SET status = 'DELETED', withdraw_datetime = NOW(), WHERE user_id = ? AND status != 'DELETED'")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;    // 회원 id
    private String email;   // 회원 이메일
    @Column(nullable = false)
    private String nickname;    // 회원 닉네임

    @Enumerated(EnumType.STRING)    // 회원상태
    @Column(nullable = false)
    private Status status = Status.PENDING; // 회원가입 시 추가 정보(닉네임) 입력해야 ACTIVE 로 변경
    @Column(nullable = false)
    private String authProvider;    // 소셜 로그인 제공자(카카오, 구글, 네이버)
    private String profileImg;  // 프로필 이미지 url

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime createDatetime;   // 회원가입 날짜
    private LocalDateTime withdrawDatetime; // 회원탈퇴 날짜
    @Column(nullable = false)
    private Long loginId;   // 소셜 로그인 사용자 고유 id

    public User(Long userId, String email, String nickname, Status status, String authProvider, String profileImg, LocalDateTime createDatetime, LocalDateTime withdrawDatetime, Long loginId) {

        this.userId = userId;
        this.email = email;
        this.nickname = nickname;
        this.status = status;
        this.authProvider = authProvider;
        this.profileImg = profileImg;
        this.createDatetime = createDatetime;
        this.withdrawDatetime = withdrawDatetime;
        this.loginId = loginId;
    }

    public static User create(Long UserId, String email, String nickname, Status status, String authProvider, String profileImg, LocalDateTime createDatetime, LocalDateTime withdrawDatetime, Long loginId) {

        return new User(UserId, email, nickname, status, authProvider, profileImg, createDatetime, withdrawDatetime, loginId);
    }

}
