package ticket.store.backend.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ticket.store.backend.dto.user.UserDto;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDto {

    private UserDto userDTO;

    private TokenPair tokenPair;
}
