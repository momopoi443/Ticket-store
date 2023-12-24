package org.example.sbdcoursework.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.sbdcoursework.dto.ticket.TicketCreationDTO;
import org.example.sbdcoursework.exception.InvalidArgumentException;
import org.example.sbdcoursework.service.JwtService;
import org.example.sbdcoursework.service.TicketService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
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
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(value = TicketController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class TicketControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TicketService ticketService;

    @MockBean
    private JwtService jwtService;

    @Captor
    ArgumentCaptor<TicketCreationDTO> creationDTOCaptor;

    private TicketCreationDTO invalidTicketCreationDTO;
    private TicketCreationDTO validTicketCreationDTO;

    @BeforeEach
    public void setUpTestData() {
        validTicketCreationDTO = TicketCreationDTO.builder()
                .userFirstName("John")
                .userLastName("Dou")
                .userEmail("test@gmail.com")
                .eventUuid(UUID.randomUUID().toString())
                .build();

        invalidTicketCreationDTO = TicketCreationDTO.builder()
                .userFirstName("")
                .userLastName("")
                .userEmail("email")
                .eventUuid("uuid")
                .build();
    }

    @Test
    public void createTicket_ShouldValidateCreationDTO() throws Exception {
        ResultActions response = mockMvc.perform(
                post(TicketController.TICKET_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                invalidTicketCreationDTO
                        ))
        );

        response.andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()", is(4)))
                .andExpect(jsonPath("$.[0].code", is(InvalidArgumentException.errorCode())))
                .andExpect(jsonPath("$.[0].description", notNullValue()));
    }

    @Test
    public void createTicket_ShouldReturnCreatedTicketUUIDAndCreatedStatus() throws Exception {
        UUID retunedUuid = UUID.randomUUID();

        when(ticketService.create(any()))
                .thenReturn(retunedUuid);

        ResultActions response = mockMvc.perform(
                post(TicketController.TICKET_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                validTicketCreationDTO
                        ))
        );

        response.andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", is(retunedUuid.toString())));
        verify(ticketService, times(1))
                .create(creationDTOCaptor.capture());
        assertEquals(validTicketCreationDTO.getEventUuid(), creationDTOCaptor.getValue().getEventUuid());
        assertEquals(validTicketCreationDTO.getUserEmail(), creationDTOCaptor.getValue().getUserEmail());
    }
}