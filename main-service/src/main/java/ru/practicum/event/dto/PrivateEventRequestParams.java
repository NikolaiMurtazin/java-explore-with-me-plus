package ru.practicum.event.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PrivateEventRequestParams {
    long userId;
    int from;
    int size;
}
