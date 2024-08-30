package ru.practicum.event.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dto.AdminEventRequestParams;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.UpdateEventAdminRequest;
import ru.practicum.event.model.EventState;
import ru.practicum.event.service.EventService;
import ru.practicum.exeption.WrongDateException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping(("/admin"))
public class AdminEventsController {

    private final EventService eventService;

    @GetMapping("/events")
    @ResponseStatus(HttpStatus.OK)
    public List<EventFullDto> getEvents(@RequestParam(value = "users", required = false) List<Long> users,
                                        @RequestParam(value = "states", required = false) List<EventState> states,
                                        @RequestParam(value = "categories", required = false) List<Long> categories,
                                        @RequestParam(value = "rangeStart", required = false)
                                        @DateTimeFormat(pattern = ("dd-MM-yyyy HH:mm:ss")) LocalDateTime rangeStart,
                                        @RequestParam(value = "rangeEnd", required = false)
                                        @DateTimeFormat(pattern = ("dd-MM-yyyy HH:mm:ss")) LocalDateTime rangeEnd,
                                        @RequestParam(value = "from", defaultValue = "0") int from,
                                        @RequestParam(value = "size", defaultValue = "10") int size) {

        Map<String, LocalDateTime> ranges = validDate(rangeStart, rangeEnd);
        AdminEventRequestParams params = AdminEventRequestParams.builder()
                .categories(categories)
                .rangeStart(ranges.get("rangeStart"))
                .rangeEnd(ranges.get("rangeEnd"))
                .from(from)
                .size(size)
                .build();
        return eventService.getAll(params);
    }

    @PatchMapping("/events/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto updateEvent(@PathVariable long eventId, @RequestBody UpdateEventAdminRequest dto) {
        return eventService.update(eventId, dto);
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
