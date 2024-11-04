package com.mendittzo.user.command.domain.repository;

import com.mendittzo.user.command.domain.aggregate.User;

public interface UserRepository {

    // 소셜 로그인 사용자의 고유 id 로 회원 조회
    User findByLoginIdAndAuthProvider(Long loginId, String authProvider);

    User save(User newUser);
}
