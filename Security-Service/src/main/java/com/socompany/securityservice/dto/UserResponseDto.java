package com.socompany.securityservice.dto;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class UserResponseDto {

    private long id;
    private String username;
    private String password;
    private String email;
    private Timestamp created;
    private Timestamp updated;

    public UserResponseDto() {}

    public UserResponseDto(long id,
                           String username,
                           String password,
                           String email,
                           Timestamp created,
                           Timestamp updated) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.created = created;
        this.updated = updated;
    }
}
