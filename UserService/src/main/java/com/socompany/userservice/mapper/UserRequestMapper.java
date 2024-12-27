package com.socompany.userservice.mapper;

import com.socompany.userservice.dto.UserRequestDto;
import com.socompany.userservice.entity.User;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;


@Component
public class UserRequestMapper implements Mapper<UserRequestDto, User>{

    @Override
    public User map(UserRequestDto fromObject) {
        User user = new User();
        user.setUsername(fromObject.getUsername());
        user.setPassword(fromObject.getPassword());
        user.setEmail(fromObject.getEmail());

        return user;
    }

    @Override
    public User map(UserRequestDto fromObj, User toObj) {
        toObj.setUsername(fromObj.getUsername());
        toObj.setPassword(fromObj.getPassword());
        toObj.setEmail(fromObj.getEmail());
        toObj.setUpdated(new Timestamp(System.currentTimeMillis()));

        return toObj;
    }
}
