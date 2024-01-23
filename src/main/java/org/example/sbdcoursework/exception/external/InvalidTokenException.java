package org.example.sbdcoursework.exception.external;

import org.springframework.security.core.AuthenticationException;

public class InvalidTokenException extends AuthenticationException {

    public InvalidTokenException(String message) {
        super(message);
    }

    public InvalidTokenException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public static String errorCode() {
        return "invalid.token";
    }
}
