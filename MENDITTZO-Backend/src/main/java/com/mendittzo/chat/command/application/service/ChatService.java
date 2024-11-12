package com.mendittzo.chat.command.application.service;

import com.mendittzo.chat.command.application.dto.ChatDTO;
import com.mendittzo.chat.command.domain.aggregate.Chat;
import com.mendittzo.chat.command.domain.repository.ChatRepository;
import com.mendittzo.user.command.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {

    private final RedisTemplate<String, String> redisTemplate;
    private final ChatRepository chatRepository;
    private final UserRepository userRepository;

    public void saveChat(ChatDTO chatDTO) {
        String chatId = UUID.randomUUID().toString();

        Chat chat = Chat.builder()
                .chatId(chatId)
                .chatContent(chatDTO.getChatContent())
                .createDatetime(chatDTO.getCreateDatetime())
                .nickname(chatDTO.getNickname())
                .chatroomId(chatDTO.getChatroomId())
                .build();

        // Redis에 Chat 엔티티 저장
        chatRepository.save(chat);

        // 채팅방 리스트에 메시지 UUID 추가
        String chatroomKey = "ChatRoom:" + chatDTO.getChatroomId();
        redisTemplate.opsForList().rightPush(chatroomKey, chatId);
    }

    public List<ChatDTO> getChats(Long chatroomId) {
        List<ChatDTO> chatList = new ArrayList<>();
        String chatroomKey = "ChatRoom:" + chatroomId;

        // 채팅방 리스트에서 모든 메시지 ID(UUID) 가져오기
        List<String> chatIds = redisTemplate.opsForList().range(chatroomKey, 0, -1);

        for (String chatId : chatIds) {
            Chat chat = chatRepository.findById(chatId).orElse(null);
            if (chat != null) {
                ChatDTO chatDTO = new ChatDTO(
                        chat.getChatContent(),
                        chat.getCreateDatetime(),
                        chat.getNickname(),
                        chat.getChatroomId()
                );
                chatList.add(chatDTO);
            }
        }
        return chatList;
    }
}
