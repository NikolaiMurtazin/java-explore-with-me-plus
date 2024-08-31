package ru.practicum.event.service;

import ru.practicum.event.dto.*;
import ru.practicum.event.model.Event;

import java.util.Collection;
import java.util.List;

public interface EventService {

//    Приватные пользователи
    List<EventShortDto> getAll(PrivateEventParams params);

    EventFullDto create(long userId, NewEventDto event);

    EventFullDto getById(long eventId);

    //Public GET /events

    List<EventShortDto> getAll(PublicEventRequestParams params);

    // GET Admin/events
    List<EventFullDto> getAll(AdminEventRequestParams params);

    //Private GET /users/{userId}/events
    //Private /users/{userId}/events/{eventsId}

    EventFullDto getById(long userId, long eventId);

    EventFullDto update(long userId, long eventId, UpdateEventUserRequest event);

    EventFullDto update(long eventId, UpdateEventAdminRequest event);

    Collection<Event> getByIds(List<Long> events);
}
