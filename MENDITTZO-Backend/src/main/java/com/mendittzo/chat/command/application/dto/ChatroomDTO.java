package com.mendittzo.chat.command.application.dto;

import com.mendittzo.chat.command.domain.aggregate.Chatroom;
import com.mendittzo.chat.command.domain.aggregate.ChatroomStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ChatroomDTO {

    private Long chatroomId;
    private Long bookId;
    private LocalDateTime openDatetime;
    private Long maxMemberCount;
    private Long currentMemberCount;
    private String title;
    private ChatroomStatus status;

    @Builder
    public ChatroomDTO(Long chatroomId, Long bookId, LocalDateTime openDatetime, Long maxMemberCount,
                       Long currentMemberCount, String title, ChatroomStatus status) {
        this.chatroomId = chatroomId;
        this.bookId = bookId;
        this.openDatetime = openDatetime;
        this.maxMemberCount = maxMemberCount;
        this.currentMemberCount = currentMemberCount;
        this.title = title;
        this.status = status;
    }

    public static ChatroomDTO fromEntity(Chatroom chatroom, Long currentMemberCount) {
        return ChatroomDTO.builder()
                .chatroomId(chatroom.getChatroomId())
                .bookId(chatroom.getBookId())
                .openDatetime(chatroom.getOpenDatetime())
                .maxMemberCount(chatroom.getMaxMemberCount())
                .currentMemberCount(currentMemberCount)
                .title(chatroom.getTitle())
                .status(chatroom.getStatus())
                .build();
    }
}
