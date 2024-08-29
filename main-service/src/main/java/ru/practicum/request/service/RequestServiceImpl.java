package ru.practicum.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.EventState;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exeption.ConflictException;
import ru.practicum.exeption.NotFoundException;
import ru.practicum.request.dto.ParticipationRequestDto;
import ru.practicum.request.dto.RequestParams;
import ru.practicum.request.mapper.RequestMapper;
import ru.practicum.request.model.Request;
import ru.practicum.request.model.RequestState;
import ru.practicum.request.repository.RequestRepository;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;

    private final RequestMapper requestMapper;

    private final UserRepository userRepository;
    private final EventRepository eventRepository;


    public List<ParticipationRequestDto> findUserRequests(long userId) {
        User user = getUser(userId);
        List<Request> allRequests = requestRepository.findByRequester(user);

        if (allRequests.isEmpty()) {
            return List.of();
        }
        return allRequests.stream().map(requestMapper::toDto).toList();
    }


    @Transactional
    public ParticipationRequestDto save(RequestParams params) {
        User requester = getUser(params.getUserId());
        Event event = getEvent(params.getEventId());
        ParticipationRequestDto dto = params.getDto();
        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new ConflictException("Event not published");
        }
        if (event.getInitiator().equals(dto.getRequester())) {
            throw new ConflictException("Requester couldn't be initiator");
        }
        if (!requestRepository.findByEventAndRequester(event, requester).isEmpty()) {
            throw new ConflictException("Repeatable request not allowed");
        }
        if (!event.getRequestModeration()) {
            dto.setStatus(RequestState.CONFIRMED);
        } else {
            dto.setStatus(RequestState.PENDING);
        }

        Request saved = requestRepository.save(requestMapper.toEntity(dto));

        return requestMapper.toDto(saved);
    }

    @Transactional
    public ParticipationRequestDto cancel(long userId, long requestId) {
        getUser(userId);
        Request request = getRequest(requestId);
        request.setStatus(RequestState.REJECTED);
        Request saved = requestRepository.save(request);
        return requestMapper.toDto(saved);
    }

    public List<ParticipationRequestDto> findUserRequestOnEvent(long userId, long eventId) {
        User user = getUser(userId);
        Event event = getEvent(eventId);
        List<Request> allRequests = requestRepository.findByEventAndEvent_Initiator(event, user);
        if (allRequests.isEmpty()) {
            return List.of();
        }
        return allRequests.stream().map(requestMapper::toDto).toList();
    }


    private Request getRequest(long requestId) {
        return requestRepository.findById(requestId).orElseThrow(() -> new NotFoundException("Request not found with id" + requestId));
    }

    private User getUser(long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found with id" + userId));
    }

    private Event getEvent(long eventId) {
        return eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Event not found with id" + eventId));
    }
}