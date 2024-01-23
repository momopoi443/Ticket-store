package org.example.sbdcoursework.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.sbdcoursework.dto.user.UserDto;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDto {

    private UserDto userDTO;

    private TokenPair tokenPair;
}
