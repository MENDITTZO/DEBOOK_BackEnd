package com.mendittzo.auth.query.application.dto;

// 카카오 인증 서버에서 받는 카카오 이용자 정보 응답

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

// 카카오 인증 서버에서 응답 받는 로그인 한 카카오 사용자 정보

@Getter
@Setter
public class KakaoUserInfoResponseDTO {

    @JsonProperty("id")
    private Long LoginId;    // 카카오 고유 사용자 ID
}
