package com.pnu.nonata.domain.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatDto {
    public enum MessageType{
        ENTER, TALK, LEAVE;
    }

    public enum RoomType{
        NOW, RESERVE;
    }

    private MessageType messageType;
    private Long roomId;
    private String message;
    private ZonedDateTime time;
    private RoomType roomType;
}
