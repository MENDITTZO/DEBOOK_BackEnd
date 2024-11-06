package com.mendittzo.auth.query.application.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// 서비스 자체 토큰 - 클라이언트로 전송용

@Getter
@Setter
@NoArgsConstructor
public class DebookTokenDTO {

    private String accessToken;
    private Long accessTokenExpiresIn;
    private String refreshToken;
    private Long refreshTokenExpiresIn;

    public DebookTokenDTO(String accessToken, Long accessTokenExpiresIn, String refreshToken, Long refreshTokenExpiresIn) {
        this.accessToken = accessToken;
        this.accessTokenExpiresIn = accessTokenExpiresIn;
        this.refreshToken = refreshToken;
        this.refreshTokenExpiresIn = refreshTokenExpiresIn;
    }

    public static DebookTokenDTO create(String accessToken, Long accessTokenExpiresIn, String refreshToken, Long refreshTokenExpiresIn) {
        return new DebookTokenDTO(accessToken, accessTokenExpiresIn, refreshToken, refreshTokenExpiresIn);
    }
}