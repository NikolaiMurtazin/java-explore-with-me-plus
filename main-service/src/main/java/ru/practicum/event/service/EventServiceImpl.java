package ru.practicum.event.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.client.StatClient;
import ru.practicum.event.dto.*;
import ru.practicum.event.mapper.EventMapper;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.EventState;
import ru.practicum.event.model.QEvent;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exeption.ConflictException;
import ru.practicum.exeption.NotFoundException;
import ru.practicum.request.dto.EventCountByRequest;
import ru.practicum.request.repository.RequestRepository;
import ru.practicum.stat.StatsParams;
import ru.practicum.stat.ViewStatsDTO;
import ru.practicum.user.dto.UserShortDto;
import ru.practicum.user.mapper.UserMapper;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final RequestRepository requestRepository;
    private final StatClient statClient;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public List<EventShortDto> getEvents(PublicEventRequestParams params) {
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
        List<String> listEndpoint = new ArrayList<>();

        List<EventCountByRequest> eventsIdWithViews;
        if (params.getOnlyAvailable()) {
            eventsIdWithViews = requestRepository.findConfirmedRequestWithoutLimitCheck(listEventIds);
        } else {
            eventsIdWithViews = requestRepository.findConfirmedRequestWithLimitCheck(listEventIds);
        }
        eventsIdWithViews.stream().peek(ev -> listEndpoint.add("/event/" + ev.getEventId())).close();
        StatsParams statsParams = StatsParams.builder()
                .uris(listEndpoint)
                .unique(false)
                .start(LocalDateTime.MIN)
                .end(LocalDateTime.now())
                .end(LocalDateTime.now()).build();

        List<ViewStatsDTO> viewStatsDTOS = statClient.getStats(statsParams);

        List<EventShortDto> returnList = eventsIdWithViews.stream().map(ev -> {
            Event finalEvent = events.get((int) ev.getEventId());
            Optional<ViewStatsDTO> first = viewStatsDTOS.stream().filter(stat -> stat.getUri().equals("/event/" + ev.getEventId())).findFirst();
            long views = first.map(ViewStatsDTO::getHits).orElse(0L);
            long countConfirmedRequest = ev.getCount();
            UserShortDto userShortDto = userMapper.toUserShortDto(finalEvent.getInitiator());
            return eventMapper.toShortDto(finalEvent, views, userShortDto, countConfirmedRequest);
        }).toList();

//        EndpointHitDTO hitDTO = new EndpointHitDTO()
//        statClient.saveStats(hitDTO);
//TODO
        return returnList;
    }

    @Override
    public EventFullDto getById(long eventId) {
        Event event = getEvent(eventId);
        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new NotFoundException("Event is not published");
        }

        Long requests = requestRepository.countConfirmedRequest(eventId);

        List<String> listEndpoint = List.of("/event/" + event.getId());
        StatsParams statsParams = StatsParams.builder()
                .uris(listEndpoint)
                .unique(false)
                .start(LocalDateTime.MIN)
                .end(LocalDateTime.now())
                .end(LocalDateTime.now()).build();
        List<ViewStatsDTO> stats = statClient.getStats(statsParams);
        long views = stats.getFirst().getHits();

        // statClient.saveStats();//TODO
        return eventMapper.toFullDto(event, views, requests);
    }

    @Override
    public List<EventFullDto> getEvents(AdminEventRequestParams params) {
        return List.of();
    }

    @Override
    public List<EventShortDto> getEvents(PrivateEventRequestParams params) {
        return List.of();
    }

    @Override
    @Transactional
    public EventFullDto createEvent(long userId, NewEventDto event) {
        User user = getUser(userId);
        if (event.getEventDate().minusHours(2).isBefore(LocalDateTime.now())) {
            throw new ConflictException("Different with now less than 2 hours");
        }

        Event entity = eventMapper.toEntity(event, LocalDateTime.now(), user, EventState.PUBLISHED);
        Event saved = eventRepository.save(entity);

        return eventMapper.toFullDto(saved, 0, 0);
    }

    @Override
    public EventFullDto getById(long userId, long eventId) {
        return null;
    }

    @Override
    @Transactional
    public EventFullDto updateEvent(long userId, long eventId, UpdateEventUserRequest event) {
        return null;
    }

    @Override
    public EventFullDto updateEvent(long eventId, UpdateEventAdminRequest event) {
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

    private User getUser(long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found with id" + userId));
    }
}
