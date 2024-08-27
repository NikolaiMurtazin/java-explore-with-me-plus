package ru.practicum.event.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.user.dto.UserShortDto;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventShortDto {
    private String annotation;

    private CategoryDto category;

    private int confirmedRequests;

    private LocalDateTime eventDate;

    private long id;

    private UserShortDto initiator;

    private boolean paid;

    private String title;

    private long views;
}