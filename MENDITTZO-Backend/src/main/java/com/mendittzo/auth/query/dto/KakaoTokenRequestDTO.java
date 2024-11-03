package com.mendittzo.auth.query.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class KakaoTokenRequestDTO {

    // 필수
    // 1. 헤더 - (1) Content_type : 요청 데이터 타입
    // 2. 본문
    // (1) grant_type : authorization_code 로 고정
    // (2) client_id : REST API 키
    // (3) redirect_uri : 인가 코드를 전달받을 서비스 서버의 URI
    // (4) code : 인가 코드 요청 받기로 얻은 인가 코드
    private String clientId;
    private String redirectUri;
    private String code;
}
