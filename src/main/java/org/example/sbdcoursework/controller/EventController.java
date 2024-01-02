package org.example.sbdcoursework.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.example.sbdcoursework.dto.event.EventCreationDTO;
import org.example.sbdcoursework.dto.event.EventDTO;
import org.example.sbdcoursework.entity.event.EventType;
import org.example.sbdcoursework.service.EventImageService;
import org.example.sbdcoursework.service.EventService;
import org.hibernate.validator.constraints.UUID;
import org.springframework.core.io.Resource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/event")
@RequiredArgsConstructor
@Validated
public class EventController {

    public static final String EVENT_PATH = "/api/event";
    public static final String EVENT_ID_PATH_VAR = "/{eventId}";
    public static final String EVENT_IMAGE_PATH = EVENT_PATH + "/image";

    private final EventService eventService;
    private final EventImageService eventImageService;

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<java.util.UUID> createEvent(@Valid EventCreationDTO creationData) {
        java.util.UUID createdEventUuid = eventService.create(
                creationData
        );

        return new ResponseEntity<>(createdEventUuid, HttpStatus.CREATED);
    }

    @GetMapping(EVENT_ID_PATH_VAR)
    public ResponseEntity<EventDTO> getEventById(@PathVariable @UUID String eventId) {
        EventDTO fetchedEventDTO = eventService.getById(
                java.util.UUID.fromString(eventId)
        );

        return ResponseEntity.ok(fetchedEventDTO);
    }

    @GetMapping("/cities")
    public ResponseEntity<List<String>> listAllCities() {
        List<String> availableCities = eventService.getAllCities();

        return ResponseEntity.ok(availableCities);
    }

    @GetMapping
    public ResponseEntity<List<EventDTO>> listEvents(
            @RequestParam(required = false) Optional<String> city,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Optional<LocalDate> date,
            @RequestParam(required = false) Optional<java.util.UUID> organizerId,
            @RequestParam List<EventType> types,
            @RequestParam @PositiveOrZero Long page
    ) {
        List<EventDTO> fetchedEvents = eventService.listAllBy(
                city, date, types, page, organizerId
        );

        return ResponseEntity.ok(fetchedEvents);
    }

    @GetMapping(EVENT_IMAGE_PATH + "/{imageFilename:.+}")
    public ResponseEntity<Resource> loadImage(@PathVariable String imageFilename) throws IOException {
        Resource image = eventImageService.loadAsResource(imageFilename);

        return ResponseEntity
                .ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "inline"
                )
                .contentType(MediaType.parseMediaType(
                        Files.probeContentType(image.getFile().toPath()))
                )
                .body(image);
    }
}
