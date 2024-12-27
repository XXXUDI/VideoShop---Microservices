package com.socompany.userservice.controller;

import com.socompany.userservice.service.UserService;
import com.socompany.userservice.dto.UserRequestDto;
import com.socompany.userservice.dto.UserResponseDto;
import com.socompany.userservice.util.UserCreationException;
import com.socompany.userservice.util.UserUpdateException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/v1/api/user")
@RequiredArgsConstructor
public class UserServiceController {

    private final UserService userService;

    @GetMapping("/getById")
    public ResponseEntity<UserResponseDto> getUserById(@RequestParam long id) {
        UserResponseDto responseDto = userService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/getByUsername")
    public ResponseEntity<UserResponseDto> getUserByUsername(@RequestParam String username) {
        UserResponseDto responseDto = userService.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return ResponseEntity.ok(responseDto);
    }


    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(@RequestBody UserRequestDto userRequestDto) throws UserCreationException {

        var response = userService.createUser(userRequestDto);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/update")
    public ResponseEntity<UserResponseDto> updateUser(@RequestBody UserRequestDto userRequestDto, @RequestParam long id) throws UserUpdateException {
        return ResponseEntity.ok(userService.updateUser(userRequestDto, id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<UserResponseDto> deleteUser(@RequestParam long id) {
        if (!userService.deleteUser(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        } else return new ResponseEntity<>(HttpStatus.OK);
    }
}
