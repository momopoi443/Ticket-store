package org.example.sbdcoursework.exception;

public class NotFoundException extends RuntimeException {

    public NotFoundException(String message) {
        super(message);
    }

    public static String errorCode() {
        return "not.found";
    }
}
