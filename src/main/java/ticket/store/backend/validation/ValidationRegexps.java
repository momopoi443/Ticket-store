package ticket.store.backend.validation;

public interface ValidationRegexps {

    String EMAIL_REGEXP = "^[a-zA-Z0-9.!#$%&'*+\\/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$";

    String PASSWORD_REGEXP = "^[A-Za-z0-9]*.{8,}$";
}
