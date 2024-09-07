package ru.practicum.event.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dto.*;
import ru.practicum.event.service.EventService;
import ru.practicum.rating.service.EventRatingService;

import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/events")
public class PrivateEventsController {
    private final EventService eventService;

    private final EventRatingService eventRatingService;

    @GetMapping
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
    public EventFullDto getById(@PathVariable("userId") long userId,
                                @PathVariable("eventId") long eventId) {
        return eventService.getById(userId, eventId);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto update(@PathVariable("userId") long userId,
                               @PathVariable("eventId") long eventId,
                               @Valid @RequestBody UpdateEventUserRequest updateEventUserRequest) {
        return eventService.update(userId, eventId, updateEventUserRequest);
    }

    @PatchMapping("/{eventId}/like")
    public void like(@Min(0) @PathVariable("userId") long userId,
                     @Min(0) @PathVariable("eventId") long eventId) {
        eventRatingService.addRating(userId, eventId, true);
    }

    @PatchMapping("/{eventId}/dislike")
    public void dislike(@Min(0) @PathVariable("userId") long userId,
                        @Min(0) @PathVariable("eventId") long eventId) {
        eventRatingService.addRating(userId, eventId, false);
    }

    @DeleteMapping("/{eventId}/like")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteLike(@Min(0) @PathVariable("userId") long userId,
                           @Min(0) @PathVariable("eventId") long eventId) {
        eventRatingService.removeRating(userId, eventId, true);
    }

    @DeleteMapping("/{eventId}/dislike")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteDislike(@Min(0) @PathVariable("userId") long userId,
                              @Min(0) @PathVariable("eventId") long eventId) {
        eventRatingService.removeRating(userId, eventId, false);
    }
}
