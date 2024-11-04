package com.mendittzo.auth.query.application.controller;

import com.mendittzo.auth.query.application.dto.KakaoLoginUrlResponseDTO;
import com.mendittzo.auth.query.application.dto.KakaoTokenResponseDTO;
import com.mendittzo.auth.query.application.dto.UserResponseDTO;
import com.mendittzo.auth.query.application.service.KakaoAuthService;
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
    // 2-2. 응답 받은 인증 코드로 카카오 인증 서버에 액세스 토큰 요청
    // 3. 액세스 토큰으로 카카오 고유 사용자 id 요청
    // 4. 카카오 고유 사용자 id 로 DB 에서 서비스 사용자 정보 조회
    @GetMapping("/auth/callback")
    public ResponseEntity<UserResponseDTO> getAuthorizationCode(@RequestParam String code) {

        UserResponseDTO userResponse = kakaoAuthService.kakaoLogin(code);
        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }
}
