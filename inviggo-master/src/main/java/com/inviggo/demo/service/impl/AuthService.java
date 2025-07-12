package com.inviggo.demo.service.impl;

import com.inviggo.demo.model.User;
import com.inviggo.demo.repository.UserRepository;
import com.inviggo.demo.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtil jwtUtil;

    public String registerUser(String username, String password,String phoneNumber){
        if(userRepository.findByUsername(username).isPresent()){
            throw new RuntimeException("Username is already taken");
        }

        User user = new User(username, passwordEncoder.encode(password), phoneNumber);

        userRepository.save(user);
        return "User registered successfully.";
    }


    public String loginUser(String username, String password) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isEmpty() || !passwordEncoder.matches(password, userOptional.get().getPassword())) {
            throw new RuntimeException("Invalid username or password.");
        }

        return jwtUtil.generateToken(username);
    }
}
