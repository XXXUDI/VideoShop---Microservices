package com.socompany.securityservice.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.socompany.securityservice.dto.UserRequestDto;
import com.socompany.securityservice.dto.UserResponseDto;
import com.socompany.securityservice.service.AuthService;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    private final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> register(@RequestBody UserRequestDto userRequestDto) {
        LOGGER.info("Trying to register user with username: {}", userRequestDto.getUsername());

//        if (authService.getUserByUsername(userRequestDto.getUsername()).isPresent()) {
//            return new ResponseEntity<>(HttpStatus.CONFLICT);
//        }

        return ResponseEntity.ok(authService.registerUser(userRequestDto).orElse(null));
    }

    @PostMapping("/token")
    public ResponseEntity<String> getToken(@RequestBody UserRequestDto userRequestDto) {
        LOGGER.info("Received get token request for {}", userRequestDto.getUsername());

        try {
            Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userRequestDto.getUsername(), userRequestDto.getPassword()));
            if (authenticate.isAuthenticated()) {
                return ResponseEntity.ok(authService.generateToken(userRequestDto.getUsername()));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Authentication failed");
        }
    }



    @GetMapping("/validate")
    public ResponseEntity<String> validateToken(@RequestParam String token) {
        LOGGER.info("Validating token: {}", token);
        authService.validateToken(token);
        return ResponseEntity.ok("Token is valid");
    }
}
