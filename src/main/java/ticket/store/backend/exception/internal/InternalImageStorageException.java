package ticket.store.backend.exception.internal;

public class InternalImageStorageException extends RuntimeException {
    public InternalImageStorageException(Throwable cause) {
        super(cause);
    }

    public InternalImageStorageException(String message) {
        super(message);
    }
}
