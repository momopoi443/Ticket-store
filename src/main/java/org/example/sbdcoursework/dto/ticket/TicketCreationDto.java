package org.example.sbdcoursework.dto.ticket;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.UUID;

import static org.example.sbdcoursework.validation.ValidationErrorMessages.INVALID_EMAIL_MESSAGE;
import static org.example.sbdcoursework.validation.ValidationErrorMessages.INVALID_UUID_MESSAGE;
import static org.example.sbdcoursework.validation.ValidationErrorMessages.NOT_BLANK_MESSAGE;
import static org.example.sbdcoursework.validation.ValidationErrorMessages.NOT_NULL_MESSAGE;
import static org.example.sbdcoursework.validation.ValidationRegexps.EMAIL_REGEXP;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TicketCreationDto {

    @NotBlank(message = NOT_BLANK_MESSAGE)
    private String userFirstName;

    @NotBlank(message = NOT_BLANK_MESSAGE)
    private String userLastName;

    @NotNull(message = NOT_NULL_MESSAGE)
    @Email(regexp = EMAIL_REGEXP, message = INVALID_EMAIL_MESSAGE)
    private String userEmail;

    @NotNull(message = NOT_NULL_MESSAGE)
    @UUID(message = INVALID_UUID_MESSAGE)
    private String eventUuid;
}
