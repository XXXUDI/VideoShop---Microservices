package com.socompany.securityservice.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.socompany.securityservice.dto.UserRequestDto;
import com.socompany.securityservice.dto.UserResponseDto;


// Now, url is requesting to Api Gateway. ->
@FeignClient(name="UserMicroservice", url = "http://localhost:8080/v1/api/user")
public interface UserService {

    @GetMapping("/getById")
    UserResponseDto getUserById(@RequestParam long id);

    @GetMapping("/getByUsername")
    UserResponseDto getUserByUsername(@RequestParam String username);

    @PostMapping
    UserResponseDto createUser(UserRequestDto userRequestDto);

    @PutMapping("/update")
    UserResponseDto updateUser(UserRequestDto userRequestDto);

    @DeleteMapping("/delete")
    UserResponseDto deleteUser(long id);
}

