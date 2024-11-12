package com.mendittzo.chat.command.domain.repository;

import com.mendittzo.chat.command.domain.aggregate.ChatroomUserInfo;
import com.mendittzo.chat.command.domain.aggregate.ChatroomUserInfoStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatroomUserInfoRepository extends JpaRepository<ChatroomUserInfo, Long> {

    long countByChatroom_ChatroomIdAndChatroomUserInfoStatus(Long chatroomId, ChatroomUserInfoStatus status);

    Optional<ChatroomUserInfo> findByChatroom_ChatroomIdAndUserIdAndChatroomUserInfoStatus(Long chatroomId, Long userId, ChatroomUserInfoStatus status);

    boolean existsByChatroom_ChatroomIdAndUserIdAndChatroomUserInfoStatus(Long chatroomId, Long userId, ChatroomUserInfoStatus status);
}
