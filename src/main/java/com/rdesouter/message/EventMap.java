package com.rdesouter.message;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.Map;

public class EventMap {
    @JsonProperty("messageMap")
    Map<String, String> messageMap = new HashMap<>();

    String get(String key) {
        return this.messageMap.get(key);
    }
}