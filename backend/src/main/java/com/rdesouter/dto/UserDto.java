package com.rdesouter.dto;

public class UserDto {

    private String login;
    private String role;

    public UserDto() {
    }

    public UserDto(String login, String role) {
        this.login = login;
        this.role = role;
    }

    public String getLogin() {
        return login;
    }
    public String getRole() {
        return role;
    }

}
