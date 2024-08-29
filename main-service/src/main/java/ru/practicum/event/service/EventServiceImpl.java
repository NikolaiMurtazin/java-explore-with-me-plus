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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventServiceImpl implements EventService {

    private final EventsRepository eventsRepository;
    private final EventMapper eventMapper;



    public findAll(EventParams params){
        QEvent event = QEvent.event;
        List<BooleanExpression> conditions = new ArrayList<>();

        int from = params.getFrom();
        int size = params.getSize();

        conditions.add(event.status.eq("PUBLISHED"));

        BooleanExpression finalConditional = conditions.stream().reduce(BooleanExpression::and).get();
        PageRequest pageRequest = PageRequest.of(from>0 ?from/size:0, size );

        conditions.add(event.eventDate.after(params.getRangeStart()));
        conditions.add(event.eventDate.before(params.getRangeEnd()));
        if(params.gettext()!=null){
            conditions.add(event.description.contain(text));//TODO регистр букв
            conditions.add(event.annotation.contain(text));
        }
        if(params.getCategories()!=null || !params.getCategories().isEmpty()){
            conditions.add(event.category.in(params.getCategories()));
        }
        if(params.getPaid()!=null){
            conditions.add(event.paid.eq(params.getPaid()));
        }

        List<Event> events = eventRepository.findAll(finalConditional,pageRequest);


// TODO add 2 request to database


        if (params.getOnlyAvailible()==true){
            List<> requests = requestRepository.findRequestWhereEventIn(List<Event> event, )
            @Query("SELECT request_id, COUNT(request_id) FROM requests r WHERE r.event IN ?1 AND r.status= 'CONFIRMED' GROUP BY request_id")
        }else{
            List<> requests = requestRepository.findRequestWhereEventIn(List<Event> event )
            @Query("SELECT request_id, COUNT(request_id) FROM requests r WHERE r.event IN ?1 AND GROUP BY request_id")
        }

        statClient.get()
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
        return eventsRepository.findAllById(events);
    }

    private Event getEvent(long eventId) {
        return eventsRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event not found with id: " + eventId));
    }
}