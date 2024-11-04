package com.mendittzo.user.command.application.dto;

import com.mendittzo.user.command.domain.aggregate.Status;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class UserCreateRequestDTO {
    private final Long userId;
    private final String email;
    private final String nickname;
    private final Status status;
    private final String authProvider;
    private final String profileImg;
    private final LocalDateTime createDatetime;
    private final LocalDateTime withdrawDatetime;
    private final Long loginId;
}
