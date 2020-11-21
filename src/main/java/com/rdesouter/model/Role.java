package com.rdesouter.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Role {

    public Long id;
    public String name;

    public Role(
            @JsonProperty(value = "id") Long id,
            @JsonProperty(value = "name") String name
    ) {
        this.id = id;
        this.name = name;
    }
}
