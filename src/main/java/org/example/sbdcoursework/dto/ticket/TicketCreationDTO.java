package org.example.sbdcoursework.dto.ticket;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.UUID;

@Getter
@Setter
public class TicketCreationDTO {

    @NotBlank
    String userFirstName;

    @NotBlank
    String userLastName;

    @NotNull
    @Email(regexp = "^[a-zA-Z0-9.!#$%&'*+\\/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$")
    String userEmail;

    @NotNull
    @UUID
    String eventUuid;
}
