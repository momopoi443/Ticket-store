package org.example.sbdcoursework.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.*;
import org.example.sbdcoursework.entity.event.EventType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventCreationDTO {

    @NotBlank
    String name;

    @NotNull
    EventType type;

    @NotNull
    @Pattern(regexp = "^[a-zA-Z]+(?:[\\s-][a-zA-Z]+)*$")
    String city;

    @NotBlank
    String cityAddress;

    @NotBlank
    String description;

    @NotBlank
    String imageName;

    @NotNull
    @Future
    @JsonFormat(
            shape = JsonFormat.Shape.STRING,
            pattern = "yyyy-MM-dd'T'HH:mm"
    )
    LocalDateTime date;

    @NotNull
    @Positive
    BigDecimal ticketPrice;

    @NotNull
    @Positive
    Long maxTicketAmount;

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
