package ru.practicum.request.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestParamsCreate {

    private long userId;
    private long eventId;
    private ParticipationRequestDto dto;

}