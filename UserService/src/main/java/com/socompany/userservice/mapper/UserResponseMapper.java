package com.socompany.userservice.mapper;

import com.socompany.userservice.dto.UserResponseDto;
import com.socompany.userservice.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserResponseMapper implements Mapper<User, UserResponseDto>{

    @Override
    public UserResponseDto map(User fromObject) {
        UserResponseDto userResponseDto = new UserResponseDto();

        userResponseDto.setId(fromObject.getId());
        userResponseDto.setUsername(fromObject.getUsername());
        userResponseDto.setPassword(fromObject.getPassword());
        userResponseDto.setEmail(fromObject.getEmail());
        userResponseDto.setCreated(fromObject.getCreated());
        userResponseDto.setUpdated(fromObject.getUpdated());

        return userResponseDto;
    }
}
