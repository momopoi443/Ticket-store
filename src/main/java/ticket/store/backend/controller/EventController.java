package ticket.store.backend.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import ticket.store.backend.dto.event.EventCreationDto;
import ticket.store.backend.dto.event.EventDto;
import ticket.store.backend.entity.event.EventType;
import ticket.store.backend.service.EventImageService;
import ticket.store.backend.service.EventService;
import org.hibernate.validator.constraints.UUID;
import org.springframework.core.io.Resource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static ticket.store.backend.validation.ValidationErrorMessages.INVALID_UUID_MESSAGE;

@RestController
@RequestMapping("/api/event")
@RequiredArgsConstructor
@Validated
public class EventController {

    private final EventService eventService;
    private final EventImageService eventImageService;

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<java.util.UUID> createEvent(@Valid EventCreationDto creationDto) {
        java.util.UUID createdEventUuid = eventService.create(creationDto);

        return new ResponseEntity<>(createdEventUuid, HttpStatus.CREATED);
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventDto> getEventById(@PathVariable @UUID(message = INVALID_UUID_MESSAGE) String eventId) {
        EventDto fetchedEventDto = eventService.getById(java.util.UUID.fromString(eventId));

        return ResponseEntity.ok(fetchedEventDto);
    }

    @GetMapping("/cities")
    public ResponseEntity<List<String>> listAllCities() {
        List<String> availableCities = eventService.getAllCities();

        return ResponseEntity.ok(availableCities);
    }

    @GetMapping
    public ResponseEntity<List<EventDto>> listEvents(
            @RequestParam(required = false) Optional<String> city,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Optional<LocalDate> date,
            @RequestParam(required = false) Optional<java.util.UUID> organizerId,
            @RequestParam List<EventType> types,
            @RequestParam(defaultValue = "true") Boolean isConfirmed,
            @RequestParam @PositiveOrZero(message = "Number can't be negative") Long page
    ) {
        List<EventDto> fetchedEvents = eventService.listAllBy(city, date, types, page, organizerId, isConfirmed);

        return ResponseEntity.ok(fetchedEvents);
    }

    @GetMapping("/image/{imageFilename:.+}")
    public ResponseEntity<Resource> loadImage(@PathVariable String imageFilename) throws IOException {
        Resource fetchedImage = eventImageService.loadAsResource(imageFilename);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline")
                .contentType(MediaType.parseMediaType(Files.probeContentType(fetchedImage.getFile().toPath())))
                .body(fetchedImage);
    }

    @PatchMapping("/{eventId}/confirm")
    public ResponseEntity<Void> confirmEvent(@PathVariable @UUID(message = INVALID_UUID_MESSAGE) String eventId) {
        eventService.confirm(java.util.UUID.fromString(eventId));

        return ResponseEntity.noContent().build();
    }
}
