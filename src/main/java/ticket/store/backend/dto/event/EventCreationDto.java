package ticket.store.backend.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ticket.store.backend.entity.event.EventType;
import org.hibernate.validator.constraints.UUID;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static ticket.store.backend.validation.ValidationErrorMessages.FUTURE_MESSAGE;
import static ticket.store.backend.validation.ValidationErrorMessages.INVALID_CITY_NAME;
import static ticket.store.backend.validation.ValidationErrorMessages.INVALID_UUID_MESSAGE;
import static ticket.store.backend.validation.ValidationErrorMessages.NOT_BLANK_MESSAGE;
import static ticket.store.backend.validation.ValidationErrorMessages.NOT_NULL_MESSAGE;
import static ticket.store.backend.validation.ValidationErrorMessages.POSITIVE_MESSAGE;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventCreationDto {

    @NotBlank(message = NOT_BLANK_MESSAGE)
    private String name;

    @NotNull(message = NOT_NULL_MESSAGE)
    private EventType type;

    @NotNull(message = NOT_NULL_MESSAGE)
    @Pattern(regexp = "^[a-zA-Z]+(?:[\\s-][a-zA-Z]+)*$", message = INVALID_CITY_NAME)
    private String city;

    @NotBlank(message = NOT_BLANK_MESSAGE)
    private String cityAddress;

    @NotBlank(message = NOT_BLANK_MESSAGE)
    private String description;

    @NotNull(message = NOT_NULL_MESSAGE)
    @UUID(message = INVALID_UUID_MESSAGE)
    private String organizerId;

    @NotNull(message = NOT_NULL_MESSAGE)
    private MultipartFile image;

    @NotNull(message = NOT_NULL_MESSAGE)
    @Future(message = FUTURE_MESSAGE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime date;

    @NotNull(message = NOT_NULL_MESSAGE)
    @Positive(message = POSITIVE_MESSAGE)
    private BigDecimal ticketPrice;

    @NotNull(message = NOT_NULL_MESSAGE)
    @Positive(message = POSITIVE_MESSAGE)
    private Long maxTicketAmount;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EventCreationDto that)) return false;

        return getName() != null ? getName().equals(that.getName()) : that.getName() == null;
    }

    @Override
    public int hashCode() {
        return getName() != null ? getName().hashCode() : 0;
    }
}
