package com.learn.authService.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class AuthService {

    private Logger log = Logger.getLogger(AuthService.class.getName());

    private final JwtService jwtService;

    public AuthService(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    public String generateToken(String username) {
        return jwtService.generateToken(username);
    }

    public boolean validateToken(String token) {
        log.info("Validating token: " + token);
        return jwtService.isTokenValid(token);
    }
}
