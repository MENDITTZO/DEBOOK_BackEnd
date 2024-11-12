package com.mendittzo.chat.command.application.service;

import com.mendittzo.chat.command.application.dto.ChatroomDTO;
import com.mendittzo.chat.command.domain.aggregate.Chatroom;
import com.mendittzo.chat.command.domain.aggregate.ChatroomStatus;
import com.mendittzo.chat.command.domain.aggregate.ChatroomUserInfo;
import com.mendittzo.chat.command.domain.aggregate.ChatroomUserInfoStatus;
import com.mendittzo.chat.command.domain.repository.ChatroomRepository;
import com.mendittzo.chat.command.domain.repository.ChatroomUserInfoRepository;
import com.mendittzo.user.command.domain.aggregate.User;
import com.mendittzo.user.command.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatroomService {

    private final ChatroomRepository chatroomRepository;
    private final ChatroomUserInfoRepository chatroomUserInfoRepository;
    private final UserRepository userRepository;

    @Transactional
    public ChatroomDTO createChatRoom(ChatroomDTO chatroomDTO) {
        Chatroom chatRoom = Chatroom.builder()
                .bookId(chatroomDTO.getBookId())
                .title(chatroomDTO.getTitle())
                .maxMemberCount(chatroomDTO.getMaxMemberCount())
                .openDatetime(LocalDateTime.now())
                .status(ChatroomStatus.ACTIVE)
                .build();

        Chatroom savedChatRoom = chatroomRepository.save(chatRoom);
        return ChatroomDTO.fromEntity(savedChatRoom, 0L);
    }

    @Transactional(readOnly = true)
    public List<ChatroomDTO> getAllChatrooms() {
        return chatroomRepository.findAll()
                .stream()
                .map(chatroom -> {
                    long activeMemberCount = chatroomUserInfoRepository.countByChatroom_ChatroomIdAndChatroomUserInfoStatus(
                            chatroom.getChatroomId(), ChatroomUserInfoStatus.ACTIVE);
                    return ChatroomDTO.fromEntity(chatroom, activeMemberCount);
                })
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ChatroomDTO getChatroomById(Long id) {
        Chatroom chatroom = chatroomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Chat room not found"));

        long activeMemberCount = chatroomUserInfoRepository.countByChatroom_ChatroomIdAndChatroomUserInfoStatus(
                id, ChatroomUserInfoStatus.ACTIVE);
        return ChatroomDTO.fromEntity(chatroom, activeMemberCount);
    }

    @Transactional
    public ChatroomDTO enterChatroom(Long chatroomId, Long userId) {
        Chatroom chatroom = chatroomRepository.findById(chatroomId)
                .orElseThrow(() -> new RuntimeException("Chat room not found"));
        User user = userRepository.findByLoginId(userId);

        long activeMemberCount = chatroomUserInfoRepository.countByChatroom_ChatroomIdAndChatroomUserInfoStatus(
                chatroomId, ChatroomUserInfoStatus.ACTIVE);
        if (activeMemberCount >= chatroom.getMaxMemberCount()) {
            throw new RuntimeException("Chat room is full");
        }

        if (!chatroomUserInfoRepository.existsByChatroom_ChatroomIdAndUserIdAndChatroomUserInfoStatus(
                chatroomId, user.getUserId(), ChatroomUserInfoStatus.ACTIVE)) {
            ChatroomUserInfo userInfo = new ChatroomUserInfo(chatroom, user.getUserId(), ChatroomUserInfoStatus.ACTIVE);
            chatroomUserInfoRepository.save(userInfo);
            return ChatroomDTO.fromEntity(chatroom, activeMemberCount + 1);
        }

        return ChatroomDTO.fromEntity(chatroom, activeMemberCount);
    }

    @Transactional
    public ChatroomDTO leaveChatroom(Long chatroomId, Long userId) {
        User user = userRepository.findByLoginId(userId);
        ChatroomUserInfo userInfo = chatroomUserInfoRepository
                .findByChatroom_ChatroomIdAndUserIdAndChatroomUserInfoStatus(
                        chatroomId, user.getUserId(), ChatroomUserInfoStatus.ACTIVE)
                .orElseThrow(() -> new RuntimeException("User is not in the chat room"));

        userInfo.deactivate();
        chatroomUserInfoRepository.save(userInfo);

        Chatroom chatroom = userInfo.getChatroom();
        long activeMemberCount = chatroomUserInfoRepository.countByChatroom_ChatroomIdAndChatroomUserInfoStatus(
                chatroomId, ChatroomUserInfoStatus.ACTIVE);
        return ChatroomDTO.fromEntity(chatroom, activeMemberCount);
    }
}
