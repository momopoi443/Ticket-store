package ticket.store.backend.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ticket.store.backend.entity.user.UserRole;

import static ticket.store.backend.validation.ValidationErrorMessages.INVALID_EMAIL_MESSAGE;
import static ticket.store.backend.validation.ValidationErrorMessages.INVALID_PASSWORD_MESSAGE;
import static ticket.store.backend.validation.ValidationErrorMessages.NOT_BLANK_MESSAGE;
import static ticket.store.backend.validation.ValidationErrorMessages.NOT_NULL_MESSAGE;
import static ticket.store.backend.validation.ValidationRegexps.EMAIL_REGEXP;
import static ticket.store.backend.validation.ValidationRegexps.PASSWORD_REGEXP;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserCreationDto {

    @NotBlank(message = NOT_BLANK_MESSAGE)
    private String firstName;

    @NotBlank(message = NOT_BLANK_MESSAGE)
    private String lastName;

    @NotNull(message = NOT_NULL_MESSAGE)
    @Email(regexp = EMAIL_REGEXP, message = INVALID_EMAIL_MESSAGE)
    private String email;

    @NotNull(message = NOT_NULL_MESSAGE)
    @Pattern(regexp = PASSWORD_REGEXP, message = INVALID_PASSWORD_MESSAGE)
    private String password;

    @NotNull(message = NOT_NULL_MESSAGE)
    private UserRole role;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserCreationDto that)) return false;

        return getEmail() != null ? getEmail().equals(that.getEmail()) : that.getEmail() == null;
    }

    @Override
    public int hashCode() {
        return getEmail() != null ? getEmail().hashCode() : 0;
    }
}
