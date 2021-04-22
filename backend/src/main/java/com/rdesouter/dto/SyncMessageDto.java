package com.rdesouter.dto;

import com.rdesouter.model.MessagePortion;
import com.rdesouter.model.SyncEvent;

public class SyncMessageDto {

    private MessagePortion messagePortion;
    private SyncEvent syncEvent;
    private UserDto userDto;

    public SyncMessageDto() {
    }

    public SyncMessageDto(MessagePortion messagePortion, SyncEvent syncEvent, UserDto userDto) {
        this.messagePortion = messagePortion;
        this.syncEvent = syncEvent;
        this.userDto = userDto;
    }

    public MessagePortion getMessagePortion() {
        return messagePortion;
    }
    public SyncEvent getSyncEvent() {
        return syncEvent;
    }
    public UserDto getUserDto() {
        return userDto;
    }

}
