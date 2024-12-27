package com.socompany.securityservice.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Builder
@Getter @Setter
public class UserRequestDto {
    private String username;
    private String password;
    private String email;

    public UserRequestDto() {}

    public UserRequestDto(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }
}