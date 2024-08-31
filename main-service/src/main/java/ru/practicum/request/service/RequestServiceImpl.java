package ru.practicum.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.EventState;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exeption.ConflictException;
import ru.practicum.exeption.NotFoundException;
import ru.practicum.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.request.dto.ParticipationRequestDto;
import ru.practicum.request.dto.RequestParamsCreate;
import ru.practicum.request.dto.RequestParamsUpdate;
import ru.practicum.request.mapper.RequestMapper;
import ru.practicum.request.model.Request;
import ru.practicum.request.model.RequestStatus;
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

    @Override
    public List<ParticipationRequestDto> findUserRequests(long userId) {
        User user = getUser(userId);
        List<Request> allRequests = requestRepository.findByRequester(user);

        if (allRequests.isEmpty()) {
            return List.of();
        }
        return allRequests.stream().map(requestMapper::toDto).toList();
    }


    @Transactional
    @Override
    public ParticipationRequestDto save(RequestParamsCreate params) {
        User requester = getUser(params.getUserId());
        Event event = getEvent(params.getEventId());
        ParticipationRequestDto dto = params.getDto();
        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new ConflictException("Event not published");
        }
        if (event.getInitiator().equals(dto.getRequester())) {
            throw new ConflictException("Requester couldn't be initiator");
        }
        List<RequestStatus> states = List.of(RequestStatus.PENDING, RequestStatus.CONFIRMED);
        if (!requestRepository.findByEventAndRequesterAndStatusIn(event, requester, states).isEmpty()) {
            throw new ConflictException("Repeatable request not allowed");
        }
        checkEventRequestLimit(event);
        if (!event.isRequestModeration()) {
            dto.setStatus(RequestStatus.CONFIRMED);
        } else {
            dto.setStatus(RequestStatus.PENDING);
        }

        Request saved = requestRepository.save(requestMapper.toEntity(dto));

        return requestMapper.toDto(saved);
    }

    private void checkEventRequestLimit(Event event) {
        if (requestRepository.isParticipantLimitReached(event.getId(), event.getParticipantLimit())) {
            throw new ConflictException("Request limit reached");
        }
    }

    @Override
    public EventRequestStatusUpdateResult updateStatus(RequestParamsUpdate params) {
        User user = getUser(params.getUserId());
        Event event = getUserEvent(params.getEventId(), user);
        checkEventRequestLimit(event);
        return null;
    }

    @Transactional
    @Override
    public ParticipationRequestDto cancel(long userId, long requestId) {
        getUser(userId);
        Request request = getRequest(requestId);
        request.setStatus(RequestStatus.REJECTED);
        Request saved = requestRepository.save(request);
        return requestMapper.toDto(saved);
    }

    @Override
    public List<ParticipationRequestDto> findRequestsOnUserEvent(long userId, long eventId) {
        User user = getUser(userId);
        Event event = getUserEvent(eventId, user);

        List<Request> allRequests = requestRepository.findByEvent(event);
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

    private Event getUserEvent(long eventId, User user) {
        return eventRepository.findByIdAndInitiator(eventId, user).orElseThrow(() -> new NotFoundException("Event not found with id" + eventId + "for user" + user.getId()));
    }
}