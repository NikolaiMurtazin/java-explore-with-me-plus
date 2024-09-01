package ru.practicum.request.service;

import ru.practicum.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.request.dto.ParticipationRequestDto;
import ru.practicum.request.dto.RequestParamsUpdate;

import java.util.List;

public interface RequestService {

    List<ParticipationRequestDto> getAll(long userId);

    ParticipationRequestDto create(long userId, long eventId);

    ParticipationRequestDto cancel(long userId, long requestId);

    List<ParticipationRequestDto> findUserRequests(long userId);

    EventRequestStatusUpdateResult updateStatus(RequestParamsUpdate params);


    List<ParticipationRequestDto> findRequestsOnUserEvent(long userId, long eventId);

}