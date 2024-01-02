package org.example.sbdcoursework.entity.event;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "events")
@Getter
@Setter
@NoArgsConstructor
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @Column(name = "uuid", nullable = false, unique = true, updatable = false)
    private UUID uuid;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private EventType type;

    @Column(name = "city", nullable = false)
    private String city;

    @Column(name = "cityAddress", nullable = false)
    private String cityAddress;

    @Column(name = "description", nullable = false, length = 300)
    private String description;

    @Column(name = "organizerId", nullable = false)
    private UUID organizerId;

    @Column(name = "imageName", nullable = false)
    private String imageFilename;

    @Column(name = "date", nullable = false)
    private LocalDateTime date;

    @Column(name = "ticketPrice", nullable = false)
    private BigDecimal ticketPrice;

    @Column(name = "maxTicketAmount", nullable = false)
    private Long maxTicketAmount;

    public Event(UUID uuid) {
        this.uuid = uuid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Event event)) return false;

        return getUuid().equals(event.getUuid());
    }

    @Override
    public int hashCode() {
        return getUuid().hashCode();
    }
}
