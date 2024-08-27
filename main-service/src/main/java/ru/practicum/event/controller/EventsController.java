package ru.practicum.event.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.service.EventService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/events")
public class EventsController {
    private final EventService eventService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<EventFullDto> getEvents(@RequestParam(value = "text", required = false) String text,
                                        @RequestParam(value = "categories", required = false) List<Long> categories,
                                        @RequestParam(value = "paid", required = false) Boolean paid,
                                        @RequestParam(value = "rangeStart", required = false) String rangeStart,
                                        @RequestParam(value = "rangeEnd", required = false) String rangeEnd,
                                        @RequestParam(value = "onlyAvailable", defaultValue = "false") Boolean onlyAvailable,
                                        @RequestParam(value = "sort", required = false) String sort,
                                        @RequestParam(value = "from", defaultValue = "0") int from,
                                        @RequestParam(value = "size", defaultValue = "10") int size) {
        return List.of();
    }

    @GetMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto getEvent(@PathVariable("eventId") long eventId) {
        return eventService.getById(eventId);
    }
}
