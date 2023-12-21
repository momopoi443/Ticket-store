package org.example.sbdcoursework.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.sbdcoursework.dto.event.EventCreationDTO;
import org.example.sbdcoursework.entity.event.EventType;
import org.example.sbdcoursework.exception.InvalidArgumentException;
import org.example.sbdcoursework.service.EventService;
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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(value = EventController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class EventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private EventService eventService;

    @MockBean
    private JwtService jwtService;

    private EventCreationDTO invalidEventCreationDTO;
    private EventCreationDTO validEventCreationDTO;

    @BeforeEach
    public void setUpTestData() {
        invalidEventCreationDTO = EventCreationDTO.builder()
                .name("")
                .type(null)
                .city("...")
                .cityAddress("")
                .description("")
                .imageName("")
                .date(LocalDateTime.now().minusYears(1))
                .ticketPrice(BigDecimal.valueOf(-1))
                .maxTicketAmount(-1L)
                .build();

        validEventCreationDTO = EventCreationDTO.builder()
                .name("Concert")
                .type(EventType.CONCERT)
                .city("Kiev")
                .cityAddress("Some address")
                .description("Some description")
                .imageName("name.png")
                .date(LocalDateTime.now().plusYears(1))
                .ticketPrice(BigDecimal.valueOf(100))
                .maxTicketAmount(200L)
                .build();
    }

    @Test
    public void createEvent_ShouldValidateCreationData() throws Exception {
        ResultActions response = mockMvc.perform(
                post(EventController.EVENT_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                invalidEventCreationDTO
                        ))
        );

        response.andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()", is(9)))
                .andExpect(jsonPath("$.[0].code", is(InvalidArgumentException.errorCode())))
                .andExpect(jsonPath("$.[0].description", notNullValue()));
    }

    @Test
    public void createEvent_ShouldReturnCreatedUserUUIDAndCreatedStatus() throws Exception {
        UUID retunedUuid = UUID.randomUUID();

        when(eventService.create(any()))
                .thenReturn(retunedUuid);

        ResultActions response = mockMvc.perform(
                post(EventController.EVENT_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                validEventCreationDTO
                        ))
        );

        response.andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", is(retunedUuid.toString())));
        verify(eventService, times(1))
                .create(validEventCreationDTO);
    }
}