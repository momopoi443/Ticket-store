package org.example.sbdcoursework.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.*;
import org.example.sbdcoursework.entity.event.EventType;
import org.hibernate.validator.constraints.UUID;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventCreationDTO {

    @NotBlank
    private String name;

    @NotNull
    private EventType type;

    @NotNull
    @Pattern(regexp = "^[a-zA-Z]+(?:[\\s-][a-zA-Z]+)*$")
    private String city;

    @NotBlank
    private String cityAddress;

    @NotBlank
    private String description;

    @NotNull
    @UUID
    private String organizerId;

    @NotNull
    private MultipartFile image;

    @NotNull
    @Future
    @JsonFormat(
            shape = JsonFormat.Shape.STRING,
            pattern = "yyyy-MM-dd'T'HH:mm"
    )
    private LocalDateTime date;

    @NotNull
    @Positive
    private BigDecimal ticketPrice;

    @NotNull
    @Positive
    private Long maxTicketAmount;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EventCreationDTO that)) return false;

        return getName() != null ? getName().equals(that.getName()) : that.getName() == null;
    }

    @Override
    public int hashCode() {
        return getName() != null ? getName().hashCode() : 0;
    }
}
