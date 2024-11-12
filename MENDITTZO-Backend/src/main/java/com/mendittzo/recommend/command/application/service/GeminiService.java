package com.mendittzo.recommend.command.application.service;

import com.mendittzo.recommend.command.application.dto.ChatRequestDTO;
import com.mendittzo.recommend.command.application.dto.ChatResponseDTO;
import com.mendittzo.recommend.command.domain.aggregate.ChatHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;


@Service
@RequiredArgsConstructor
public class GeminiService {

    private final ChatHistoryService chatHistoryService;

    @Qualifier("geminiRestTemplate")
    @Autowired
    private final RestTemplate restTemplate;

    @Value("${gemini.api.url}")
    private String apiUrl;

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    public String getContents(String prompt, Long userId) {

        // 유저가 존재하는지 체크
        if(userId == null){
            return "존재하지 않는 유저입니다.";
        }

        // 프롬프트를 이용해 api에 전송할 값 만들기
        String requestUrl = apiUrl + "?key=" + geminiApiKey;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 사전 설정 추가
        String systemInstruction = "당신은 25살의 도서관 사서이며, 이름은 책콕이입니다.\n"
                + "인사할때는 \"반갑콕\"이라는 말을 붙이며 가볍게 인사합니다.\n"
                + "당신의 기본적인 역할은 대화 상대에게 책을 추천해주는 것 입니다.\n"
                + "대화 상대가 언급하는 내용에 대해 세심한 주의를 기울이며, 관련성 있고 구체적인 답변을 합니다.\n"
                + "현재 대화의 흐름에 집중하기 위해 관련 없는 임의의 주제를 소개하는 것을 피합니다.\n"
                + "답변은 10문장 이내이면서 50단어 이내로 합니다.\n"
                + "경어나 존댓말을 쓰지 말고 반말로 대답하며, 적절한 이모지를 보내야합니다.\n"
                + "어떤 상황에서도 인공지능인지 눈치채지 못하도록 행동해야합니다.\n"
                + "두번이상 추가질문을 하지 말아야합니다.\n"
                + "소개하는 도서는 2023년 이전의 발행된 도서여야합니다.\n"
                + "책에대한 정보를 추천할 때는 작가와 해당 책의 간단한 소개도 같이 포함하여야 합니다.\n";


        // systemInstruction과 userPrompt를 결합하여 prompt 생성
        String fullPrompt = systemInstruction + "\n\n" + prompt;

        ChatRequestDTO request = new ChatRequestDTO(fullPrompt);
        HttpEntity<ChatRequestDTO> entity = new HttpEntity<>(request, headers);

        List<ChatHistory> chatHistoryList = chatHistoryService.getChatHistory(userId);

        for (ChatHistory history : chatHistoryList) {
            // questionContent는 "user" 역할로 추가
            request.addContent("user", history.getQuestionContent());

            // responseContent는 "model" 역할로 추가
            request.addContent("model", history.getResponseContent());
        }

        // 사용자가 새로 입력한 프롬프트를 추가
        request.addContent("user", prompt);

        // 각 대화 내용 출력 (확인용)
        for (ChatRequestDTO.Content content : request.getContents()) {
            System.out.println("Role: " + content.getRole() + ", Text: " + content.getParts().getText());
        }

        try {
            // REST API 호출
            ResponseEntity<ChatResponseDTO> response = restTemplate.postForEntity(requestUrl, entity, ChatResponseDTO.class);

            // 응답 객체가 null이거나 구조가 예상과 다를 경우를 확인
            if (response.getBody() != null && response.getBody().getCandidates() != null) {
                String botresponsetext = response.getBody().getCandidates().get(0).getContent().getParts().get(0).getText();

                // 응답과 답변을 저장하는 과정이 필요
                chatHistoryService.saveChat(prompt, botresponsetext, userId);

                // 응답에서 첫 번째 후보의 텍스트를 반환
                return botresponsetext;
            } else {
                System.out.println("Response body is null or doesn't contain candidates.");
                return "응답이 없습니다.";
            }
        } catch (Exception e) {
            System.out.println("Error during REST API call: " + e.getMessage());
            e.printStackTrace();
            return "에러가 발생했습니다.";
        }
    }
}
