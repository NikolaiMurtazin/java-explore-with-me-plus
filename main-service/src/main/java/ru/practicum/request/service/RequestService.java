package ru.practicum.request.service;

import org.springframework.transaction.annotation.Transactional;
import ru.practicum.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.request.dto.ParticipationRequestDto;
import ru.practicum.request.dto.RequestParamsCreate;
import ru.practicum.request.dto.RequestParamsUpdate;

import java.util.List;

public interface RequestService {

    List<ParticipationRequestDto> findUserRequests(long userId);

    @Transactional
    ParticipationRequestDto save(RequestParamsCreate params);

    EventRequestStatusUpdateResult updateStatus(RequestParamsUpdate params);


    @Transactional
    ParticipationRequestDto cancel(long userId, long requestId);

    List<ParticipationRequestDto> findRequestsOnUserEvent(long userId, long eventId);
}