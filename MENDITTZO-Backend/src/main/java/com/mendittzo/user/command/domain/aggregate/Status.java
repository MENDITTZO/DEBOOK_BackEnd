package com.mendittzo.user.command.domain.aggregate;

public enum Status {

    ACTIVE, // 활성화(회원가입 완료)
    PENDING,    // 회원가입 중 추가 정보(닉네임) 입력 안 됨
    SUSPENDED,  // 제재 중
    DELETED // 탈퇴
}
