package com.inviggo.demo.request;

import lombok.Data;
import lombok.Getter;

@Getter
public class LoginRequest {
    private String username;
    private String password;

    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }
}



