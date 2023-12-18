package org.example.sbdcoursework.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.sbdcoursework.entity.user.UserRole;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private UUID uuid;

    private String firstName;

    private String lastName;

    private String email;

    private UserRole role;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserDTO userDTO)) return false;

        return getUuid().equals(userDTO.getUuid());
    }

    @Override
    public int hashCode() {
        return getUuid().hashCode();
    }
}
