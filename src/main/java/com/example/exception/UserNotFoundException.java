package com.example.exception;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String login) {
        super("User with login '" + login + "' not found");
    }

    public UserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
