package org.example.sbdcoursework.exception;

import org.springframework.security.core.AuthenticationException;

public class InvalidTokenException extends AuthenticationException {

    public InvalidTokenException(String message) {
        super(message);
    }

    public static String errorCode() {
        return "invalid.token";
    }
}
