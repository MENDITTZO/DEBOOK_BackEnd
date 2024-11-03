package com.mendittzo.auth.query.controller;

import com.mendittzo.auth.query.dto.*;
import com.mendittzo.auth.query.service.KakaoAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class KakaoLoginController {

    private final KakaoAuthService kakaoAuthService;

    // 1. 카카오 인증 서버에서 카카오 로그인 URL 리턴 받기
    @GetMapping("/public/auth/kakao-url")
    public ResponseEntity<KakaoLoginUrlResponseDTO> getKakaoLoginPage() {

        KakaoLoginUrlResponseDTO loginUrlResponse = kakaoAuthService.getKakaoLoginUrl();
        return new ResponseEntity<>(loginUrlResponse, HttpStatus.OK);
    }

    // 2. 카카오 로그인 인증 코드를 리턴 받은 후 액세스 토큰 요청하기
    // 2-1. 카카오 인증 서버가 http://localhost:8080/api/v1/auth/callback?code=AUTHORIZATION_CODE
    // 으로 리디렉션 하며 인증 코드를 URL 파라미터로 전송
    @GetMapping("/auth/callback")
    public ResponseEntity<KakaoTokenResponseDTO> getAuthorizationCode(@RequestParam String code) {

        KakaoTokenResponseDTO tokenResponse = kakaoAuthService.requestAccessToken(code);
        return new ResponseEntity<>(tokenResponse, HttpStatus.OK);
    }

    // 3. 카카오 인증 서버에서 로그인 한 카카오 사용자 정보 요청하기
    @GetMapping("/auth/users/info")
    public ResponseEntity<KakaoUserInfoResponseDTO> getUserInfo(@RequestHeader("Authorization") String authorizationHeader) {

        String accessToken = authorizationHeader.replace("Bearer ", "");
        KakaoUserInfoResponseDTO userInfo = kakaoAuthService.getKakaoUserInfo(accessToken);
        return new ResponseEntity<>(userInfo, HttpStatus.OK);
    }
}
