package ru.practicum.event.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.PublicEventRequestParams;
import ru.practicum.event.model.Sort;
import ru.practicum.event.service.EventService;
import ru.practicum.exeption.WrongDateException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/events")
public class PublicEventsController {
    private final EventService eventService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<EventFullDto> getEvents(@RequestParam(value = "text", required = false) String text,
                                        @RequestParam(value = "categories", required = false) List<Long> categories,
                                        @RequestParam(value = "paid", required = false) Boolean paid,
                                        @RequestParam(value = "rangeStart", required = false)
                                        @DateTimeFormat(pattern = ("dd-MM-yyyy HH:mm:ss")) LocalDateTime rangeStart,
                                        @RequestParam(value = "rangeEnd", required = false)
                                        @DateTimeFormat(pattern = ("dd-MM-yyyy HH:mm:ss")) LocalDateTime rangeEnd,
                                        @RequestParam(value = "onlyAvailable", defaultValue = "false") Boolean onlyAvailable,
                                        @RequestParam(value = "sort", required = false) Sort sort,
                                        @RequestParam(value = "from", defaultValue = "0") int from,
                                        @RequestParam(value = "size", defaultValue = "10") int size) {

        Map<String, LocalDateTime> ranges = validDate(rangeStart, rangeEnd);
        PublicEventRequestParams params = PublicEventRequestParams.builder()
                .text(text)
                .categories(categories)
                .paid(paid)
                .rangeStart(ranges.get("rangeStart"))
                .rangeEnd(ranges.get("rangeEnd"))
                .onlyAvailable(onlyAvailable)
                .sort(sort)
                .from(from)
                .size(size)
                .build();
//        eventService.findAll(params);
        return List.of();
    }

    @GetMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto getEvent(@PathVariable("eventId") long eventId) {
        return eventService.getById(eventId);
    }

    private Map<String, LocalDateTime> validDate(LocalDateTime rangeStart, LocalDateTime rangeEnd) {
        if (rangeEnd.isBefore(rangeStart)) {
            throw new WrongDateException("Range end must be after range start");
        }
        if (rangeStart == null) {
            return Map.of("rangeStart", LocalDateTime.now(),
                    "rangeEnd", rangeEnd);
        } else {
            return Map.of("rangeStart", rangeStart,
                    "rangeEnd", rangeEnd);
        }
    }
}