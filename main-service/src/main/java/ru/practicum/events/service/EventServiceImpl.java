package ru.practicum.events.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.events.dto.EventFullDto;
import ru.practicum.events.mapper.EventMapper;
import ru.practicum.events.model.Event;
import ru.practicum.events.repository.EventsRepository;
import ru.practicum.exeption.NotFoundException;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventsService {

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

    private Event getEvent(long eventId) {
        return eventsRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event not found with id: " + eventId));
    }
}
