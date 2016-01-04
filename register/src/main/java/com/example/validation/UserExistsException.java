package com.example.validation;

public class UserExistsException extends Throwable {
    public UserExistsException(final String message) {
        super(message);
    }
}
