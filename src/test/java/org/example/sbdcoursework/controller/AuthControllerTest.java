package org.example.sbdcoursework.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.sbdcoursework.dto.auth.CredentialsDTO;
import org.example.sbdcoursework.dto.auth.LoginResponsePayload;
import org.example.sbdcoursework.dto.auth.TokenPair;
import org.example.sbdcoursework.dto.user.UserDTO;
import org.example.sbdcoursework.entity.user.UserRole;
import org.example.sbdcoursework.exception.InvalidArgumentException;
import org.example.sbdcoursework.service.AuthService;
import org.example.sbdcoursework.service.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(value = AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    @MockBean
    private JwtService jwtService;

    private CredentialsDTO invalidCredentialsDTO;
    private CredentialsDTO validUserCredentialsDTO;

    @BeforeEach
    public void setUpTestData() {
        validUserCredentialsDTO = CredentialsDTO.builder()
                .email("test@gmail.com")
                .password("qwerty12345")
                .build();

        invalidCredentialsDTO = CredentialsDTO.builder()
                .email("email")
                .password("qwert")
                .build();
    }

    @Test
    public void login_ShouldValidateCredentials() throws Exception {
        ResultActions response = mockMvc.perform(
                post(AuthController.AUTH_PATH + "/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                invalidCredentialsDTO
                        ))
        );

        response.andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()", is(2)))
                .andExpect(jsonPath("$.[0].code", is(InvalidArgumentException.errorCode())))
                .andExpect(jsonPath("$.[0].description", notNullValue()));
    }

    @Test
    public void login_ShouldReturnLoginResponsePayloadAndOkStatus() throws Exception {
        UserDTO expectedUserDTO = new UserDTO(
                UUID.randomUUID(),
                "John",
                "Dou",
                "test@gmail.com",
                UserRole.CLIENT
        );
        LoginResponsePayload expectedPayload = new LoginResponsePayload(
                expectedUserDTO,
                new TokenPair(
                        "accessToken",
                        "refreshToken"
                )
        );

        when(authService.login(any())).thenReturn(expectedPayload);

        ResultActions response = mockMvc.perform(
                post(AuthController.AUTH_PATH + "/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                validUserCredentialsDTO
                        ))
        );
        response.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath(
                        "$.userDTO.uuid",
                        is(expectedUserDTO.getUuid().toString())
                ))
                .andExpect(jsonPath(
                        "$.tokenPair.accessToken",
                        is(expectedPayload.getTokenPair().getAccessToken())
                ))
                .andExpect(jsonPath(
                    "$.tokenPair.refreshToken",
                    is(expectedPayload.getTokenPair().getRefreshToken())
                ));
        verify(authService, times(1))
                .login(validUserCredentialsDTO);
    }
}