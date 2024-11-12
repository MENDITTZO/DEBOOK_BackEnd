package com.mendittzo.chat.command.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChatDTO {

    private String chatContent;
    private LocalDateTime createDatetime;
    private String nickname;
    private Long chatroomId;
}
