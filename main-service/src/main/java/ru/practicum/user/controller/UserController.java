package ru.practicum.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.service.EventService;

import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final EventService eventService;

    @GetMapping("/{userId}/events")
    @ResponseStatus(HttpStatus.OK)
    public List<EventFullDto> getEvents(@PathVariable("userId") long userId,
                                        @RequestParam(value = "from", defaultValue = "0") int from,
                                        @RequestParam(value = "size", defaultValue = "10") int size) {
        return eventService.getEvents(userId, from, size);
    }

    @PostMapping("{userId}/events")
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto createEvent(@PathVariable("userId") long userId,
                                    @Valid @RequestBody NewEventDto event) {
        return eventService.create(userId, event);
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
                                    @RequestBody NewEventDto event) {
        return eventService.update(userId, eventId, event);
    }

//    @GetMapping("{userId}/event/{eventId}/requests")
//    @ResponseStatus(HttpStatus.OK)
//    public getRequestByUserAndEventId(@PathVariable("userId") long userId,
//                                      @PathVariable("eventId") long eventId) {
//        return null;
//    TODO Доделать, когда будут готовы запросы на участие
//    }
//
//    @PatchMapping("{userId}/event/{eventId}/requests")
//    @ResponseStatus(HttpStatus.OK)
//    public UpdateStatusRequestByUserAndEventId(@PathVariable("userId") long userId,
//                                               @PathVariable("eventId") long eventId) {
//        return null;
//    TODO Доделать, когда будут готовы запросы на участие
//    }
}
