package com.socompany.securityservice.service;

import com.socompany.securityservice.dto.UserResponseDto;

import java.util.Map;

public interface JwtGeneratorInterface {

    Map<String, String> validateToken(UserResponseDto user);
}
