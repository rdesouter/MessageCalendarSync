package com.rdesouter.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.Map;

public class MessageConfig {
    @JsonProperty("messageConfig")
    public Map<String, String> map = new HashMap<>();

    public String get(String key) {
        return this.map.get(key);
    }
}
