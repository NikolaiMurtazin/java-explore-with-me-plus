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

import java.time.LocalDateTime;
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
    @Transactional
    public ParticipationRequestDto create(long userId, long evenId) { // готово, но не проверял
        User requester = getUser(userId);
        Event event = getEvent(evenId);

        if (event.getInitiator().getId().equals(userId)) {
            throw new ConflictException("The initiator of the event can't add a request to participate in his event");
        }

        if (!event.getState().equals(EventState.PUBLISH_EVENT)) {
            throw new ConflictException("Event not published");
        }

        if (event.getParticipantLimit() == 0 || event.getParticipantLimit() > event.getConfirmedRequests()) {
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
        } else {
            throw new ConflictException("The event has reached the limit of participation requests");
        }

        Request request = new Request();
        request.setRequester(requester);
        request.setEvent(event);
        request.setCreated(LocalDateTime.now());

        if (event.isRequestModeration()) {
            request.setStatus(RequestStatus.PENDING);
        } else {
            request.setStatus(RequestStatus.CONFIRMED);
        }

        eventRepository.save(event);
        return requestMapper.toParticipationRequestDto(requestRepository.save(request));
    }

    @Override
    public List<ParticipationRequestDto> findUserRequests(long userId) {
        User user = getUser(userId);
        List<Request> allRequests = requestRepository.findByRequester(user);

        if (allRequests.isEmpty()) {
            return List.of();
        }
        return allRequests.stream().map(requestMapper::toParticipationRequestDto).toList();
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
        return requestMapper.toParticipationRequestDto(saved);
    }

    @Override
    public List<ParticipationRequestDto> findRequestsOnUserEvent(long userId, long eventId) {
        User user = getUser(userId);
        Event event = getUserEvent(eventId, user);

        List<Request> allRequests = requestRepository.findByEvent(event);
        if (allRequests.isEmpty()) {
            return List.of();
        }
        return allRequests.stream().map(requestMapper::toParticipationRequestDto).toList();
    }

    @Override
    public List<ParticipationRequestDto> getAll(long userId) {
        return List.of();
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