package org.example.sbdcoursework.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class TokenPair {

    private String accessToken;

    private String refreshToken;
}
