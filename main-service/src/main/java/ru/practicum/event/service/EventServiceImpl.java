package ru.practicum.event.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.event.dto.AdminEventRequestParams;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.dto.PublicEventRequestParams;
import ru.practicum.event.mapper.EventMapper;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.QEvent;
import ru.practicum.event.repository.EventsRepository;
import ru.practicum.exeption.NotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventServiceImpl implements EventService {

    private final EventsRepository eventsRepository;
    private final EventMapper eventMapper;


    @Override
    public List<EventFullDto> find(PublicEventRequestParams params) {
        QEvent qEvent = QEvent.event;
        BooleanExpression byTextInAnnotation = qEvent.annotation.contains(params.getText());
        BooleanExpression byTextInDescription = qEvent.description.contains(params.getText());
        BooleanExpression byCategory = qEvent.category.id.in(params.getCategories());
        eventsRepository.findAll(byTextInAnnotation.and(byTextInDescription.and(byCategory)));
        return List.of();
    }

    @Override
    public List<EventFullDto> find(AdminEventRequestParams params) {
        return List.of();
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
