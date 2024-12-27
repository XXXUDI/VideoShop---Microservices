package com.socompany.userservice.util;

public class UserUpdateException extends Exception{
    public UserUpdateException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserUpdateException(String message) {
        super(message);
    }
}
