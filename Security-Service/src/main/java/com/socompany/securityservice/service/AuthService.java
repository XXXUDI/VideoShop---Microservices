package com.socompany.securityservice.service;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.socompany.securityservice.dto.UserRequestDto;
import com.socompany.securityservice.dto.UserResponseDto;

@Service
public class AuthService {

    private final UserService userService;

    private final JWTService jwtService;

    private final PasswordEncoder passwordEncoder;

    public AuthService(UserService userService, JWTService jwtService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<UserResponseDto> registerUser(UserRequestDto userRequestDto) {
        userRequestDto.setPassword(passwordEncoder.encode(userRequestDto.getPassword()));

        return Optional.of(userService.createUser(userRequestDto));
    }

    public Optional<UserResponseDto> getUserByUsername(String username) {
        return Optional.of(userService.getUserByUsername(username));
    }


    public String generateToken(String username) {
        return jwtService.generateToken(username);
    }

    public void validateToken(String token) {
        jwtService.validateToken(token);
    }
}
