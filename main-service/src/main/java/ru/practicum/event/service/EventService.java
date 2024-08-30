package ru.practicum.event.service;

import ru.practicum.event.dto.*;
import ru.practicum.event.model.Event;

import java.util.Collection;
import java.util.List;

public interface EventService {

    //Public GET /events
    List<EventShortDto> getEvents(PublicEventRequestParams params);

    //Public GET /events/{eventId}
    EventFullDto getById(long eventId);

    // GET Admin/events
    List<EventFullDto> getEvents(AdminEventRequestParams params);

    //Private GET /users/{userId}/events
    List<EventShortDto> getEvents(PrivateEventRequestParams params);

    //Private /users/{userId}/events/{eventsId}
    EventFullDto getById(long userId, long eventId);

    EventFullDto createEvent(long userId, NewEventDto event);

    EventFullDto updateEvent(long userId, long eventId, UpdateEventUserRequest event);

    EventFullDto updateEvent(long eventId, UpdateEventAdminRequest event);

    Collection<Event> getByIds(List<Long> events);
}
