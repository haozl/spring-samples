package com.example.validation;

public class EmailExistsException extends Throwable {
    public EmailExistsException(final String message) {
        super(message);
    }
}
