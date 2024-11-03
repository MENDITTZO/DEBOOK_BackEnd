package com.mendittzo.auth.query.dto;

import lombok.Getter;
import lombok.Setter;

// 3. 카카오 인증 서버에서 로그인 한 카카오 사용자 정보 요청

@Getter
@Setter
public class KakaoUserInfoRequestDTO {

    private String accessToken; // 사용자 액세스 토큰 값

}
