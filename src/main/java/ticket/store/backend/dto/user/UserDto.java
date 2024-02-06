package ticket.store.backend.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ticket.store.backend.dto.ticket.TicketDto;
import ticket.store.backend.entity.user.UserRole;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private UUID uuid;

    private String firstName;

    private String lastName;

    private String email;

    private UserRole role;

    private List<TicketDto> tickets;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserDto userDTO)) return false;

        return getUuid().equals(userDTO.getUuid());
    }

    @Override
    public int hashCode() {
        return getUuid().hashCode();
    }
}
