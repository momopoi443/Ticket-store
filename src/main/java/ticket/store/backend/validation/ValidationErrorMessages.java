package ticket.store.backend.validation;

public interface ValidationErrorMessages {

    String INVALID_EMAIL_MESSAGE = "Given invalid email";

    String INVALID_PASSWORD_MESSAGE = "Given invalid password";

    String NOT_BLANK_MESSAGE = "Value can't be blank";

    String NOT_NULL_MESSAGE = "Value can't be null";

    String INVALID_CITY_NAME = "Given invalid city name";

    String FUTURE_MESSAGE = "Date must be future";

    String POSITIVE_MESSAGE = "Number must be positive";

    String INVALID_UUID_MESSAGE = "String must be valid uuid";
}
