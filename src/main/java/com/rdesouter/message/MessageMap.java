package com.rdesouter.message;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.Map;

public class MessageMap {
    @JsonProperty("messageMap")
    public Map<String, String> messageMap = new HashMap<>();

    public String get(String key) {
        return this.messageMap.get(key);
    }
}
