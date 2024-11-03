package com.mendittzo.auth.query.dto;

// 카카오 인증 서버에서 받는 카카오 이용자 정보 응답

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KakaoUserInfoResponseDTO {

    private Long id;    // 카카오 고유 사용자 ID
}
