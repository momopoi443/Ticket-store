package org.example.sbdcoursework.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.example.sbdcoursework.entity.user.UserRole;

@Getter
@Setter
public class UserCreationDTO {

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotNull
    @Email(regexp = "^[a-zA-Z0-9.!#$%&'*+\\/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$")
    private String email;

    @NotNull
    @Pattern(regexp = "^[A-Za-z0-9]*.{8,}$")
    private String password;

    @NotNull
    private UserRole role;
}
