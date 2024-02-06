package ticket.store.backend.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.UUID;

@Document("tickets")
@Getter
@Setter
@NoArgsConstructor
public class Ticket {

    @Id
    private String id;

    @Indexed(unique = true)
    private UUID uuid;

    private UUID userUuid;

    private String userFirstName;

    private String userLastName;

    private String userEmail;

    private UUID eventUuid;

    private LocalDateTime createdAt;

    public Ticket(UUID uuid) {
        this.uuid = uuid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Ticket ticket)) return false;

        return getUuid().equals(ticket.getUuid());
    }

    @Override
    public int hashCode() {
        return getUuid().hashCode();
    }
}
