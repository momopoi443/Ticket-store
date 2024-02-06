package ticket.store.backend.exception.external;

public class InvalidUserCredentialsException extends RuntimeException {

    public InvalidUserCredentialsException(String message) {
        super(message);
    }

    public static String errorCode() {
        return "invalid.user.credentials";
    }
}
