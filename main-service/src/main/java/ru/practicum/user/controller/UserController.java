package ru.practicum.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dto.*;
import ru.practicum.event.service.EventService;
import ru.practicum.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.request.dto.ParticipationRequestDto;
import ru.practicum.request.dto.RequestParamsUpdate;
import ru.practicum.request.service.RequestService;

import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final EventService eventService;
    private final RequestService requestService;

    @GetMapping("/{userId}/events")
    @ResponseStatus(HttpStatus.OK)
    public List<EventShortDto> getEvents(@PathVariable("userId") long userId,
                                         @RequestParam(value = "from", defaultValue = "0") int from,
                                         @RequestParam(value = "size", defaultValue = "10") int size) {

        return eventService.getEvents(new PrivateEventRequestParams(userId, from, size));
    }

    @PostMapping("{userId}/events")
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto createEvent(@PathVariable("userId") long userId,
                                    @Valid @RequestBody NewEventDto event) {
        return eventService.createEvent(userId, event);
    }

    @GetMapping("{userId}/events/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto getEventById(@PathVariable("userId") long userId,
                                     @PathVariable("eventId") long eventId) {
        return eventService.getById(userId, eventId);
    }

    @PatchMapping("{userId}/events/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto updateEvent(@PathVariable("userId") long userId,
                                    @PathVariable("eventId") long eventId,
                                    @RequestBody UpdateEventUserRequest event) {
        return eventService.updateEvent(userId, eventId, event);
    }

    @GetMapping("{userId}/event/{eventId}/requests")
    @ResponseStatus(HttpStatus.OK)
    public List<ParticipationRequestDto> getRequestsOnUserEvent(@PathVariable("userId") long userId,
                                                                @PathVariable("eventId") long eventId) {

        return requestService.findRequestsOnUserEvent(userId, eventId);
    }

    @PatchMapping("{userId}/event/{eventId}/requests")
    @ResponseStatus(HttpStatus.OK)
    public EventRequestStatusUpdateResult UpdateStatusRequestByUserAndEventId(@PathVariable("userId") long userId,
                                                                              @PathVariable("eventId") long eventId,
                                                                              @RequestBody EventRequestStatusUpdateRequest updateDto) {

        return requestService.updateStatus(new RequestParamsUpdate(userId, eventId, updateDto));
    }
}