package org.example.sbdcoursework.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.sbdcoursework.dto.user.UserDTO;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponsePayload {

    UserDTO userDTO;

    TokenPair tokenPair;
}
