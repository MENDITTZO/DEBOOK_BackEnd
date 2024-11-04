package com.mendittzo.auth.query.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// 카카오 인증 서버에 카카오 로그인 URL 요청 시 응답

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor  // 역직렬화(JSON -> JAVA 객체) 시 기본 생성자 필요
public class KakaoLoginUrlResponseDTO {

    private String kakaoLoginUrl;   // 카카오 로그인 URL

}
