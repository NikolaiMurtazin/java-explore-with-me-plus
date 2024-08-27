package ru.practicum.event.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.mapper.EventMapper;
import ru.practicum.event.model.Event;
import ru.practicum.event.repository.EventsRepository;
import ru.practicum.exeption.NotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventsRepository eventsRepository;
    private final EventMapper eventMapper;

    @Override
    public EventFullDto find() {
        return eventsRepository.findBy();//TODO
    }

    @Override
    public EventFullDto findById(long eventId) {

        return eventMapper.toFullDto(getEvent(eventId));
    }

    @Override
    public List<EventFullDto> getEvents(long userId, int from, int size) {
        return List.of();
    }

    @Override
    @Transactional
    public EventFullDto create(long userId, NewEventDto event) {
        return null;
    }

    @Override
    public EventFullDto getById(long userId, long eventId) {
        return null;
    }

    @Override
    public EventFullDto getById(long eventId) {
        return null;
    }

    @Override
    @Transactional
    public EventFullDto update(long userId, long eventId, NewEventDto event) {
        return null;
    }

    @Override
    @Transactional
    public EventFullDto update(long eventId, NewEventDto event) {
        return null;
    }

    private Event getEvent(long eventId) {
        return eventsRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event not found with id: " + eventId));
    }
}
