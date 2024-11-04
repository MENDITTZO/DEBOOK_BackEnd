package com.mendittzo.auth.query.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

// 카카오 인증 서버에 보내는 카카오 로그인 url 요청

@Getter
@Setter
@AllArgsConstructor
public class KakaoLoginUrlRequestDTO {

    private String clientId;
    private String redirectUri;
}
