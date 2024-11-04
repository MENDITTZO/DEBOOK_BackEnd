package com.mendittzo.user.command.mapper;

import com.mendittzo.user.command.application.dto.UserCreateRequestDTO;
import com.mendittzo.user.command.domain.aggregate.Status;
import com.mendittzo.user.command.domain.aggregate.User;

public class UserMapper {
    public static User toEntity(UserCreateRequestDTO userRequest) {
        return User.create(
                userRequest.getUserId(),
                userRequest.getEmail(),
                userRequest.getNickname(),
                userRequest.getStatus() != null ? userRequest.getStatus() : Status.PENDING,
                userRequest.getAuthProvider(),
                userRequest.getProfileImg(),
                userRequest.getCreateDatetime(),
                userRequest.getWithdrawDatetime(),
                userRequest.getLoginId()
        );
    }
}
