package com.socompany.securityservice.config;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.socompany.securityservice.dto.UserResponseDto;
import com.socompany.securityservice.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserService userService;

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LOGGER.info("Received request to load user by username {}", username);
        Optional<UserResponseDto> user = Optional.of(userService.getUserByUsername(username));
        return user.map(CustomUserDetails::new).orElseThrow(() -> new UsernameNotFoundException(username));
    }
}
