package com.rdesouter.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Person {

    public Long id;
    public String name;
    public String email;
    public String refreshToken;

    public Person(
            @JsonProperty(value = "id") Long id,
            @JsonProperty(value = "name") String name,
            @JsonProperty(value = "email") String email,
            @JsonProperty(value = "refreshToken") String refreshToken
    ) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.refreshToken = refreshToken;
    }
}
