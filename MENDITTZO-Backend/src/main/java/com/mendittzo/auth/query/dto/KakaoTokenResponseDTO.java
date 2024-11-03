package com.mendittzo.auth.query.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

// 카카오 인증 서버에 액세스 토큰 요청 시 응답

@Getter
@Setter
// Jackson 라이브러리가 JSON 응답을 KakaoTokenResponseDTO 객체로 변환하므로 생성자 필요 X
public class KakaoTokenResponseDTO {

    @JsonProperty("token_type")
    private String tokenType;   // 토큰 타입 (bearer 로 고정)
    @JsonProperty("access_token")
    private String accessToken; // 사용자 액세스 토큰 값
    @JsonProperty("expires_in")
    private Long expiresIn; // 액세스 토큰의 만료 시간(초)
    @JsonProperty("refresh_token")
    private String refreshToken;    // 사용자 리프레시 토큰 값
    @JsonProperty("refresh+token_expires_in")
    private Long refreshTokenExpiresIn; // 리프레시 토큰 만료 시간(초)
}
