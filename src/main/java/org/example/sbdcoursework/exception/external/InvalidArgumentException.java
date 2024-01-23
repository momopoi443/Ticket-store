package org.example.sbdcoursework.exception.external;

public class InvalidArgumentException extends RuntimeException {

    public InvalidArgumentException(String message) {
        super(message);
    }

    public static String errorCode() {
        return "invalid.argument";
    }
}