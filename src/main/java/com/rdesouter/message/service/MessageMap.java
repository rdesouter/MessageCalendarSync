package com.rdesouter.message.service;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.Map;

public class MessageMap {
    @JsonProperty("messageMap")
    Map<String, String> messageMap = new HashMap<>();

    String get(String key) {
        return this.messageMap.get(key);
    }
}
