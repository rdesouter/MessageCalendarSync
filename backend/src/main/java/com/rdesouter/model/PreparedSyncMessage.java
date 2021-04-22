package com.rdesouter.model;

import com.google.api.services.gmail.model.Message;
import com.google.api.services.gmail.model.MessagePart;

import java.util.HashMap;
import java.util.List;

public class PreparedSyncMessage {

    private Message message;
    private MessageHeaders headers;
    private List<MessagePart> parts;
    private MessageConfig config;
    private HashMap<String, String> extraced;

    public PreparedSyncMessage() {
    }

    public Message getMessage() {
        return message;
    }
    public PreparedSyncMessage setMessage(Message message) {
        this.message = message;
        return this;
    }

    public MessageHeaders getHeaders() {
        return headers;
    }
    public PreparedSyncMessage setHeaders(MessageHeaders headers) {
        this.headers = headers;
        return this;
    }

    public List<MessagePart> getParts() {
        return parts;
    }
    public PreparedSyncMessage setParts(List<MessagePart> parts) {
        this.parts = parts;
        return this;
    }

    public MessageConfig getConfig() {
        return config;
    }
    public PreparedSyncMessage setConfig(MessageConfig config) {
        this.config = config;
        return this;
    }

    public HashMap<String, String> getExtraced() {
        return extraced;
    }
    public PreparedSyncMessage setExtracted(HashMap<String,String> extraced) {
        this.extraced = extraced;
        return this;
    }


}
