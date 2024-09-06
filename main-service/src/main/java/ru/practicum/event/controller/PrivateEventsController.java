package ru.practicum.event.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dto.*;
import ru.practicum.event.service.EventService;

import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/events")
public class PrivateEventsController {
    private final EventService eventService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<EventShortDto> getAll(@PathVariable("userId") long userId,
                                      @RequestParam(value = "from", defaultValue = "0") int from,
                                      @RequestParam(value = "size", defaultValue = "10") int size) {

        return eventService.getAll(new PrivateEventParams(userId, from, size));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto create(@PathVariable("userId") long userId,
                               @Valid @RequestBody NewEventDto event) {
        return eventService.create(userId, event);
    }

    @GetMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto getById(@PathVariable("userId") long userId,
                                @PathVariable("eventId") long eventId) {
        return eventService.getById(userId, eventId);
    }

    @PatchMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto update(@PathVariable("userId") long userId,
                               @PathVariable("eventId") long eventId,
                               @Valid @RequestBody UpdateEventUserRequest updateEventUserRequest) {
        return eventService.update(userId, eventId, updateEventUserRequest);
    }

    @PatchMapping("/{eventId}/like")
    @ResponseStatus(HttpStatus.OK)
    public void like(@Min(0) @PathVariable("userId") long userId,
                     @Min(0) @PathVariable("eventId") long eventId) {
        eventService.estimate(userId, eventId, true);
    }

    @PatchMapping("/{eventId}/dislike")
    @ResponseStatus(HttpStatus.OK)
    public void dislike(@Min(0) @PathVariable("userId") long userId,
                        @Min(0) @PathVariable("eventId") long eventId) {
        eventService.estimate(userId, eventId, false);
    }

    @DeleteMapping("/{eventId}/like")
    @ResponseStatus(HttpStatus.OK)
    public void deletelike(@Min(0) @PathVariable("userId") long userId,
                           @Min(0) @PathVariable("eventId") long eventId) {
        eventService.deleteEstimete(userId, eventId, true);
    }

    @DeleteMapping("/{eventId}/dislike")
    @ResponseStatus(HttpStatus.OK)
    public void deleteDislike(@Min(0) @PathVariable("userId") long userId,
                              @Min(0) @PathVariable("eventId") long eventId) {
        eventService.deleteEstimete(userId, eventId, false);
    }
}
