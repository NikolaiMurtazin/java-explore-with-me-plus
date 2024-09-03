package ru.practicum.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.exeption.ValidEventDate;
import ru.practicum.location.model.Location;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewEventDto {
    @NotBlank
    @Size(min = 20, max = 2000, message = "The length of the annotation must be at least 20 characters and no more than 2000 characters")
    private String annotation;

    @NotNull(message = "Category can't be blank")
    private Long category;

    @NotBlank(message = "Description can't be blank")
    @Size(min = 20, max = 7000, message = "The length of the description must be at least 20 characters and no more than 7000 characters")
    private String description;

    @NotNull(message = "EventDate can't be blank")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @ValidEventDate
    private LocalDateTime eventDate;

    @NotNull(message = "Location can't be blank")
    private Location location;

    private boolean paid = false;

    @PositiveOrZero(message = "participant must be more or equal 0")
    private Integer participantLimit = 0;

    private Boolean requestModeration = true;

    @NotBlank(message = "Title can't be blank")
    @Size(min = 3, max = 120, message = "The length of the title must be at least 3 characters and no more than 120 characters")
    private String title;
}
