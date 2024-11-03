package com.mendittzo.user.query.dto;

import com.mendittzo.user.command.domain.aggregate.Status;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


// 회원과 관련된 모든 정보에 대한 DTO
// 다른 API 에서 회원의 일부 정보를 필요로 할 때 사용한다.
@Getter
@Setter
public class UserInfoDTO {

    private Long userId;    // 회원 id
    private String email;   // 회원 이메일
    private String nickName;    // 회원 닉네임
    private Status status = Status.PENDING; // 회원가입 시 추가 정보(닉네임) 입력해야 ACTIVE 로 변경
    private String authProvider;    // 소셜 로그인 제공자(카카오, 구글, 네이버)
    private String profileImg;  // 프로필 이미지 url
    private LocalDateTime createDatetime;   // 회원가입 날짜
    private LocalDateTime withdrawDatetime; // 회원탈퇴 날짜
}
