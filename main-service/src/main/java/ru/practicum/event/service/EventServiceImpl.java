package ru.practicum.event.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.client.StatClient;
import ru.practicum.event.dto.AdminEventRequestParams;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.dto.PublicEventRequestParams;
import ru.practicum.event.mapper.EventMapper;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.EventState;
import ru.practicum.event.model.QEvent;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exeption.NotFoundException;
import ru.practicum.request.dto.ParticipationRequestDto;
import ru.practicum.request.repository.RequestRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final RequestRepository requestRepository;
    private final StatClient statClient;

    @Override
    public List<ParticipationRequestDto> findAllB(PublicEventRequestParams params) {
        QEvent event = QEvent.event;
        List<BooleanExpression> conditions = new ArrayList<>();

        int from = params.getFrom();
        int size = params.getSize();

        conditions.add(event.state.eq(EventState.PUBLISHED));

        BooleanExpression finalConditional = conditions.stream().reduce(BooleanExpression::and).get();
        PageRequest pageRequest = PageRequest.of(from > 0 ? from / size : 0, size);

        conditions.add(event.eventDate.after(params.getRangeStart()));
        conditions.add(event.eventDate.before(params.getRangeEnd()));
        if (params.getText() != null) {
            conditions.add(event.description.containsIgnoreCase(params.getText()));
            conditions.add(event.annotation.containsIgnoreCase(params.getText()));
        }
        if (params.getCategories() != null || !params.getCategories().isEmpty()) {
            conditions.add(event.category.id.in(params.getCategories()));
        }
        if (params.getPaid() != null) {
            conditions.add(event.paid.eq(params.getPaid()));
        }


        List<Event> events = eventRepository.findAll(finalConditional, pageRequest).getContent();
        List<Long> listEventIds = events.stream().map(Event::getId).toList();

// TODO add 2 request to database

        statClient.getStats()
        if (params.getOnlyAvailable()) {
            requestRepository.findConfirmedRequestWhereEventIn(listEventIds);
        } else {
            List<> requests = requestRepository.findRequestWhereEventIn(List < Event > event)
        }

        statClient.saveStats()

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

    @Override
    public Collection<Event> getByIds(List<Long> events) {
        return eventRepository.findAllById(events);
    }

    private Event getEvent(long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event not found with id: " + eventId));
    }
}
