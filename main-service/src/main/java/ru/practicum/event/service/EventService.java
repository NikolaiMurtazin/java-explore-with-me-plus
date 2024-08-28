package ru.practicum.event.service;

import ru.practicum.event.dto.AdminEventRequestParams;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.dto.PublicEventRequestParams;

import java.util.List;

public interface EventService {

    List<EventFullDto> find(PublicEventRequestParams params);

    List<EventFullDto> find(AdminEventRequestParams params);

    EventFullDto findById(long id);

    List<EventFullDto> getEvents(long userId, int from, int size);

    EventFullDto create(long userId, NewEventDto event);

    EventFullDto getById(long userId, long eventId);

    EventFullDto getById(long eventId);

    EventFullDto update(long userId, long eventId, NewEventDto event);

    EventFullDto update(long eventId, NewEventDto event);
}
