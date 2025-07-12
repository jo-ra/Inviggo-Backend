package com.inviggo.demo.controller;

import com.inviggo.demo.request.LoginRequest;
import com.inviggo.demo.request.RegisterRequest;
import com.inviggo.demo.service.impl.AuthService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import org.springframework.beans.factory.annotation.Autowired;


@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public String register(@RequestBody @Valid RegisterRequest registerRequest) {
        return authService.registerUser(registerRequest.getUsername(),
                registerRequest.getPassword(), registerRequest.getPhoneNumber());
    }

    @PostMapping("/login")
    public String login(@RequestBody @Valid LoginRequest loginRequest) {
        return authService.loginUser(loginRequest.getUsername(), loginRequest.getPassword());
    }
}