package com.mendittzo.auth.query.application.dto;

import com.mendittzo.user.command.domain.aggregate.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class UserResponseDTO {
    private Long userId;
    private String email;
    private String nickname;
    private String status;
    private String authProvider;
    private String profileImg;
    private LocalDateTime createDatetime;
    private LocalDateTime withdrawDatetime;
    private Long loginId;

    public UserResponseDTO(User user) {
        this.userId = user.getUserId();
        this.email = user.getEmail();
        this.nickname = user.getNickName();
        this.status = user.getStatus().toString();
        this.authProvider = user.getAuthProvider();
        this.profileImg = user.getProfileImg();
        this.createDatetime = user.getCreateDatetime();
        this.withdrawDatetime = user.getWithdrawDatetime();
        this.loginId = user.getLoginId();
    }
}