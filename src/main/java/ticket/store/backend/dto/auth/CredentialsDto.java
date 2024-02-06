package ticket.store.backend.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static ticket.store.backend.validation.ValidationErrorMessages.INVALID_EMAIL_MESSAGE;
import static ticket.store.backend.validation.ValidationErrorMessages.INVALID_PASSWORD_MESSAGE;
import static ticket.store.backend.validation.ValidationErrorMessages.NOT_BLANK_MESSAGE;
import static ticket.store.backend.validation.ValidationRegexps.EMAIL_REGEXP;
import static ticket.store.backend.validation.ValidationRegexps.PASSWORD_REGEXP;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CredentialsDto {

    @NotBlank(message = NOT_BLANK_MESSAGE)
    @Email(regexp = EMAIL_REGEXP, message = INVALID_EMAIL_MESSAGE)
    private String email;

    @NotBlank(message = NOT_BLANK_MESSAGE)
    @Pattern(regexp = PASSWORD_REGEXP, message = INVALID_PASSWORD_MESSAGE)
    private String password;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CredentialsDto that)) return false;

        if (getEmail() != null ? !getEmail().equals(that.getEmail()) : that.getEmail() != null) return false;
        return getPassword() != null ? getPassword().equals(that.getPassword()) : that.getPassword() == null;
    }

    @Override
    public int hashCode() {
        int result = getEmail() != null ? getEmail().hashCode() : 0;
        result = 31 * result + (getPassword() != null ? getPassword().hashCode() : 0);
        return result;
    }
}
