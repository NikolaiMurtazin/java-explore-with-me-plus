package ru.practicum.events.service;

import ru.practicum.events.dto.EventFullDto;

public interface EventsService {

    EventFullDto find();

    EventFullDto findById(long id);
}
