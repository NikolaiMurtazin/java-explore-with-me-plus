package ru.practicum.event.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.category.model.Category;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.client.StatClient;
import ru.practicum.event.dto.*;
import ru.practicum.event.mapper.EventMapper;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.EventState;
import ru.practicum.event.model.QEvent;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exeption.ConflictException;
import ru.practicum.exeption.NotFoundException;
import ru.practicum.location.model.Location;
import ru.practicum.location.repository.LocationRepository;
import ru.practicum.request.dto.EventCountByRequest;
import ru.practicum.request.repository.RequestRepository;
import ru.practicum.stat.StatsParams;
import ru.practicum.stat.ViewStatsDTO;
import ru.practicum.user.mapper.UserMapper;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

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
    private final CategoryRepository categoryRepository;
    private final LocationRepository locationRepository;

    @Override
    public List<EventShortDto> getAll(PublicEventRequestParams params) {
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
            return eventMapper.toEventShortDto(finalEvent);
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
        return eventMapper.toEventFullDto(event);
    }

    @Override
    public List<EventFullDto> getAll(AdminEventRequestParams params) {
        return List.of();
    }

//    Приватные пользователи

    @Override
    public List<EventShortDto> getAll(PrivateEventParams params) { // готов
        QEvent event = QEvent.event;
        List<BooleanExpression> conditions = new ArrayList<>();
        conditions.add(event.initiator.id.eq(params.getUserId()));
        BooleanExpression finalCondition = conditions.stream()
                .reduce(BooleanExpression::and)
                .orElse(null);

        PageRequest pageRequest = PageRequest.of(params.getFrom() / params.getSize(), params.getSize());

        Iterable<Event> compilationsIterable = eventRepository.findAll(finalCondition, pageRequest);

        List<Event> compilations = StreamSupport.stream(compilationsIterable.spliterator(), false)
                .toList();

        return compilations.stream()
                .map(eventMapper::toEventShortDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EventFullDto create(long userId, NewEventDto eventDto) { // готово
        User initiator = getUser(userId);
        if (eventDto.getEventDate().minusHours(2).isBefore(LocalDateTime.now())) {
            throw new ConflictException("Different with now less than 2 hours");
        }
        Category category = categoryRepository.findById(eventDto.getCategory())
                .orElseThrow(() -> new NotFoundException("Category with id= " + eventDto.getCategory() + " was not found"));
        Location location = locationRepository.save(eventDto.getLocation());

        Event event = eventMapper.toEvent(eventDto, category, location, initiator, EventState.PENDING, LocalDateTime.now());
        Event saved = eventRepository.save(event);

        return eventMapper.toEventFullDto(saved);
    }
//
    @Override
    public EventFullDto getById(long userId, long eventId) {
        return null;
    }

    @Override
    @Transactional
    public EventFullDto update(long userId, long eventId, UpdateEventUserRequest event) {
        return null;
    }

    @Override
    public EventFullDto update(long eventId, UpdateEventAdminRequest event) {
        return null;
    }

    @Override
    public List<Event> getByIds(List<Long> events) {
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
