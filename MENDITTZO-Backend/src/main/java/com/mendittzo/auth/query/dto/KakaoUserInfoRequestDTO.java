package com.mendittzo.auth.query.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class KakaoUserInfoRequestDTO {

    private String accessToken; // 사용자 액세스 토큰 값

}
