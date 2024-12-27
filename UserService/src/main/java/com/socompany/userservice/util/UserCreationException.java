package com.socompany.userservice.util;

public class UserCreationException extends Exception {
    public UserCreationException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserCreationException(String message) {
        super(message);
    }
}
