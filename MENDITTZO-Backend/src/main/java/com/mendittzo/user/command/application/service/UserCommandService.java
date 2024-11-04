package com.mendittzo.user.command.application.service;

import com.mendittzo.user.command.application.dto.UserCreateRequestDTO;
import com.mendittzo.user.command.domain.aggregate.User;
import com.mendittzo.user.command.domain.repository.UserRepository;
import com.mendittzo.user.command.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserCommandService {

    private final UserRepository userRepository;

    // 새로운 유저 생성
    public User registerNewUser(UserCreateRequestDTO userRequest) {

        User newUser = UserMapper.toEntity(userRequest);
        return userRepository.save(newUser);
    }
}
