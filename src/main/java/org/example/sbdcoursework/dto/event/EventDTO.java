package org.example.sbdcoursework.dto.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.sbdcoursework.entity.event.EventType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventDTO {

    private UUID uuid;

    private String name;

    private EventType type;

    private String city;

    private String cityAddress;

    private String description;

    private String imageUrl;

    private LocalDateTime date;

    private BigDecimal ticketPrice;

    private Long availableTicketAmount;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EventDTO eventDTO)) return false;

        return getUuid().equals(eventDTO.getUuid());
    }

    @Override
    public int hashCode() {
        return getUuid().hashCode();
    }
}
