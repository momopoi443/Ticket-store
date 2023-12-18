package org.example.sbdcoursework.exception;

public class InvalidUserCredentialsException extends RuntimeException {

    public InvalidUserCredentialsException(String message) {
        super(message);
    }

    public static String errorCode() {
        return "invalid.user.credentials";
    }
}
