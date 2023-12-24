package org.example.sbdcoursework.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.sbdcoursework.dto.user.UserCreationDTO;
import org.example.sbdcoursework.entity.user.UserRole;
import org.example.sbdcoursework.exception.InvalidArgumentException;
import org.example.sbdcoursework.service.JwtService;
import org.example.sbdcoursework.service.UserService;
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

@WebMvcTest(value = UserController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @MockBean
    private JwtService jwtService;

    private UserCreationDTO invalidUserCreationDTO;
    private UserCreationDTO validUserCreationDTO;

    @BeforeEach
    public void setUpTestData() {
        validUserCreationDTO = UserCreationDTO.builder()
                .firstName("John")
                .lastName("Dou")
                .email("test@gmail.com")
                .password("qwerty12345")
                .role(UserRole.CLIENT)
                .build();

        invalidUserCreationDTO = UserCreationDTO.builder()
                .firstName("")
                .lastName("")
                .email("email")
                .password("qwert")
                .role(null)
                .build();
    }

    @Test
    public void createUser_ShouldValidateCreationData() throws Exception {
        ResultActions response = mockMvc.perform(
                post(UserController.USER_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                invalidUserCreationDTO
                        ))
        );

        response.andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()", is(5)))
                .andExpect(jsonPath("$.[0].code", is(InvalidArgumentException.errorCode())))
                .andExpect(jsonPath("$.[0].description", notNullValue()));
    }

    @Test
    public void createUser_ShouldReturnCreatedUserUUIDAndCreatedStatus() throws Exception {
        UUID retunedUuid = UUID.randomUUID();

        when(userService.register(any()))
                .thenReturn(retunedUuid);

        ResultActions response = mockMvc.perform(
                post(UserController.USER_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                validUserCreationDTO
                        ))
        );

        response.andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", is(retunedUuid.toString())));
        verify(userService, times(1))
                .register(validUserCreationDTO);
    }
}