package com.mendittzo.auth.query.service;

import com.mendittzo.auth.query.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class KakaoAuthService {

    // 필수 쿼리 파라미터
    // (1) client_id : REST API 키
    // (2) redirect_uri : 인가 코드를 전달받을 서비스 서버의 URI
    // (3) response_type : code 로 고정

    @Value("${kakao.client_id.rest_api_key}")
    private String clientId;

    @Value("${kakao.redirect_uri}")
    private String redirectUri;

    // 1-1. 카카오 인증 서버에 카카오 로그인 url 요청하는 메소드
    public KakaoLoginUrlResponseDTO getKakaoLoginUrl() {

        // todo: 확인용. 나중에 삭제
        System.out.println("yml: clientId: " + clientId);
        System.out.println("yml: redirectUri: " + redirectUri);

        // yml 에 저장한 client_id, redirect_uri 값을  DTO 로 전달
        // todo: 이러면 DTO 쓰는 의미가 있나?
        KakaoLoginUrlRequestDTO loginUrlRequest = new KakaoLoginUrlRequestDTO(clientId, redirectUri);

        // todo: 확인용. 나중에 삭제
        System.out.println("DTO: clientId: " + loginUrlRequest.getClientId());
        System.out.println("DTO: redirectUri: " + loginUrlRequest.getRedirectUri());

        String loginUrl = "https://kauth.kakao.com/oauth/authorize?client_id="
                + loginUrlRequest.getClientId()
                + "&redirect_uri=" + loginUrlRequest.getRedirectUri()
                + "&response_type=code";
        return new KakaoLoginUrlResponseDTO(loginUrl);
    }

    // 2-2. 응답 받은 인증 코드로 카카오 인증 서버에 액세스 토큰 요청하는 메소드
    public KakaoTokenResponseDTO requestAccessToken(String code) {

        // WebClient : 서버에서 외부 API 로 요청 보낼 때 사용
        WebClient webClient = WebClient.create();

        KakaoTokenRequestDTO tokenRequest = new KakaoTokenRequestDTO(
                clientId,
                redirectUri,
                code
        );

        // todo: 확인용. 나중에 삭제
        System.out.println("-------- request access token ---------");
        System.out.println("yml: clientId: " + clientId);
        System.out.println("yml: redirectUri: " + redirectUri);

        // todo: 확인용. 나중에 삭제
        System.out.println("DTO: clientId: " + tokenRequest.getClientId());
        System.out.println("DTO: redirectUri: " + tokenRequest.getRedirectUri());
        System.out.println("DTO: code: " + tokenRequest.getCode());

        try {
            return webClient.post()
                    .uri("https://kauth.kakao.com/oauth/token")
                    .header("Content-type", "application/x-www-form-urlencoded;charset=utf-8")
                    // application/x-www-form-urlencoded;charset=utf-8 형식으로 body 삽입
                    .body(BodyInserters.fromFormData("grant_type", "authorization_code")
                            .with("client_id", tokenRequest.getClientId())
                            .with("redirect_uri", tokenRequest.getRedirectUri())
                            .with("code", tokenRequest.getCode()))
                    .retrieve()
                    .bodyToMono(KakaoTokenResponseDTO.class)    // KakaoTokenResponseDTO 타입으로 응답 받기
                    // todo: 로그
                    .doOnNext(token -> System.out.println("Received access token response: " + token))
                    .block();   // 동기 방식으로 액세스 토큰 받기
            // todo: content-secret 설정을 할까말까

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(" 액세스 토큰 요청 중 에러: " + e.getMessage());
            return null;
        }
    }

    // 3-1. 카카오 인증 서버에서 로그인 한 카카오 사용자 정보 요청하는 메소드
    public KakaoUserInfoResponseDTO getKakaoUserInfo(KakaoUserInfoRequestDTO InfoRequest) {

        // WebClient : 서버에서 외부 API 로 요청 보낼 때 사용
        WebClient webClient = WebClient.create();

        return webClient.get()
                .uri("https://kapi.kakao.com/v2/user/me")
                .header("Authorization", "Bearer " + InfoRequest.getAccessToken())
                .retrieve()
                .bodyToMono(KakaoUserInfoResponseDTO.class) // KakaoUserInfoResponseDTO 타입으로 응답 받기
                .block();
    }

}
