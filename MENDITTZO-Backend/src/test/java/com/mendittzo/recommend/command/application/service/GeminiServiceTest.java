package com.mendittzo.recommend.command.application.service;


import com.mendittzo.recommend.command.application.dto.ChatResponseDTO;
import com.mendittzo.recommend.command.domain.aggregate.ChatHistory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;

import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GeminiServiceTest {

    @Mock
    private ChatHistoryService chatHistoryService;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private GeminiService geminiService;

    @Value("${gemini.api.url}")
    private String apiUrl;

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    private final Long userId = 1L;
    private final String prompt = "책을 추천해줘";
    private final String systemInstruction = "당신은 25살의 도서관 사서이며, 이름은 책콕이입니다...";  // 필요한 사전 설정의 일부

    @BeforeEach
    void setUp() {
        geminiService = new GeminiService(chatHistoryService, restTemplate);
    }

    @Test
    // API 정상 호출 테스트
    void testGetContentsWithValidResponse() {
        // Mock ChatHistoryService to return previous chat history
        List<ChatHistory> mockHistoryList = new ArrayList<>();
        mockHistoryList.add(new ChatHistory("책 추천 해줘", "좋은 책은 ...", LocalDateTime.now(), userId));
        when(chatHistoryService.getChatHistory(userId)).thenReturn(mockHistoryList);

        // Prepare mock response from the API
        ChatResponseDTO.Parts part = new ChatResponseDTO.Parts();
        part.setText("반갑콕! 추천 책은...");

        ChatResponseDTO.Content content = new ChatResponseDTO.Content();
        content.setParts(List.of(part));
        content.setRole("assistant");

        ChatResponseDTO.Candidate candidate = new ChatResponseDTO.Candidate();
        candidate.setContent(content);

        ChatResponseDTO mockResponseBody = new ChatResponseDTO();
        mockResponseBody.setCandidates(List.of(candidate));

        // Configure RestTemplate to return the mock response
        ResponseEntity<ChatResponseDTO> mockResponse = new ResponseEntity<>(mockResponseBody, HttpStatus.OK);
        when(restTemplate.postForEntity(any(String.class), any(HttpEntity.class), eq(ChatResponseDTO.class)))
                .thenReturn(mockResponse);

        // Call the method to test
        String response = geminiService.getContents(prompt, userId);

        // Verify that ChatHistoryService and RestTemplate were called
        verify(chatHistoryService).getChatHistory(userId);
        verify(restTemplate).postForEntity(any(String.class), any(HttpEntity.class), eq(ChatResponseDTO.class));

        // Verify that the response is as expected
        assertEquals("반갑콕! 추천 책은...", response);
    }




    @Test
    // 유저가 존재 하지 않을 시 오류메시지 호출 테스트
    void testGetContentsWithNullUserId() {
        // Call the method with a null userId
        String response = geminiService.getContents(prompt, null);

        // Verify that the response is the expected error message
        assertEquals("존재하지 않는 유저입니다.", response);
    }

    @Test
    // API 응답 결과가 비어있는 경우 처리 테스트
    void testGetContentsWithEmptyApiResponse() {
        // Mock ChatHistoryService to return previous chat history
        List<ChatHistory> mockHistoryList = new ArrayList<>();
        when(chatHistoryService.getChatHistory(userId)).thenReturn(mockHistoryList);

        // Configure RestTemplate to return a null response body
        ResponseEntity<ChatResponseDTO> mockResponse = new ResponseEntity<>(null, HttpStatus.OK);
        when(restTemplate.postForEntity(any(String.class), any(HttpEntity.class), eq(ChatResponseDTO.class)))
                .thenReturn(mockResponse);

        // Call the method to test
        String response = geminiService.getContents(prompt, userId);

        // Verify that the response is the expected message when there is no response
        assertEquals("응답이 없습니다.", response);
    }

    @Test
    //RestTemplate 예외 발생 시 오류 처리 테스트
    void testGetContentsWithException() {
        // Mock ChatHistoryService to return previous chat history
        List<ChatHistory> mockHistoryList = new ArrayList<>();
        when(chatHistoryService.getChatHistory(userId)).thenReturn(mockHistoryList);

        // Configure RestTemplate to throw an exception
        when(restTemplate.postForEntity(any(String.class), any(HttpEntity.class), eq(ChatResponseDTO.class)))
                .thenThrow(new RuntimeException("API call failed"));

        // Call the method to test
        String response = geminiService.getContents(prompt, userId);

        // Verify that the response is the expected error message
        assertEquals("에러가 발생했습니다.", response);
    }
}
