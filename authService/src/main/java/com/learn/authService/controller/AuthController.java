package com.learn.authService.controller;

import com.learn.authService.DTO.UserCredentialDTO;
import com.learn.authService.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @GetMapping("/token")
    public String getToken(UserCredentialDTO userCredentialDTO) {
        return authService.generateToken(userCredentialDTO.getUsername());
    }

    @GetMapping("/validate")
    public String validateToken(String token) {
        if ( authService.validateToken(token) )
            return "Token is valid";
        return "Token is invalid";
    }

    @PostMapping("register")
    public String registerUser(UserCredentialDTO userCredentialDTO) {
        // Logic to register the user
        return "User registered successfully";
    }
}
