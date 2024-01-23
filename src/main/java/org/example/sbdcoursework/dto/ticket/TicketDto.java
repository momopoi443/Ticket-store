package org.example.sbdcoursework.dto.ticket;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TicketDto {

    private UUID uuid;

    private UUID userUuid;

    private String userFirstName;

    private String userLastName;

    private String userEmail;

    private UUID eventUuid;

    private LocalDateTime eventDate;

    private String eventCity;

    private String eventCityAddress;

    private String eventImageUrl;

    private LocalDateTime createdAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TicketDto ticketDTO)) return false;

        return getUuid().equals(ticketDTO.getUuid());
    }

    @Override
    public int hashCode() {
        return getUuid().hashCode();
    }
}
