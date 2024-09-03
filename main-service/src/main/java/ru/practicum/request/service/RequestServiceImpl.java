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
import ru.practicum.request.dto.RequestParamsUpdate;
import ru.practicum.request.mapper.RequestMapper;
import ru.practicum.request.model.Request;
import ru.practicum.request.model.RequestStatus;
import ru.practicum.request.repository.RequestRepository;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;

    private final RequestMapper requestMapper;

    private final UserRepository userRepository;

    private final EventRepository eventRepository;

    @Override
    public List<ParticipationRequestDto> getAll(long userId) { // todo не проверял
        User user = getUser(userId);
        List<Request> requests = requestRepository.findByRequester(user);
        return requests.stream()
                .map(requestMapper::toParticipationRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ParticipationRequestDto create(long userId, long evenId) { // todo выдает ошибку 500
        User requester = getUser(userId);
        Event event = getEvent(evenId);

        if (event.getInitiator().getId().equals(userId)) {
            throw new ConflictException("The initiator of the event can't add a request to participate in his event");
        }
        if (!requestRepository.findByEventAndRequester(event, requester).isEmpty()) {
            throw new ConflictException("Repeatable request not allowed");
        }
        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new ConflictException("Event not published");
        }
        Integer countConfirmedRequest = requestRepository.countConfirmedRequest(evenId);
        if (!(event.getParticipantLimit() == 0) && event.getParticipantLimit() < countConfirmedRequest) {
            throw new ConflictException("The event has reached the limit of participation requests");
        }
        event.setConfirmedRequests(countConfirmedRequest);
        Request request = new Request();
        request.setRequester(requester);
        request.setEvent(event);
        request.setCreated(LocalDateTime.now());

        if (event.isRequestModeration() && event.getParticipantLimit() != 0) {
            request.setStatus(RequestStatus.PENDING);
        } else {
            request.setStatus(RequestStatus.CONFIRMED);
        }

        return requestMapper.toParticipationRequestDto(requestRepository.save(request));
    }

    @Transactional
    @Override
    public ParticipationRequestDto cancel(long userId, long requestId) { // todo вроде выглядит норм, но не проверял
        getUser(userId);
        Request request = getRequest(requestId);
        request.setStatus(RequestStatus.CANCELED);
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
    @Transactional
    public EventRequestStatusUpdateResult updateStatus(RequestParamsUpdate params) {
        User user = getUser(params.getUserId());
        Event event = getUserEvent(params.getEventId(), user);
        List<Long> requestIds = params.getDto().getRequestIds();
        List<ParticipationRequestDto> updatedRequests = new ArrayList<>();
        if (params.getDto().getStatus().equals(RequestStatus.REJECTED)) {
            for (Long requestId : requestIds) {
                Request request = getRequest(requestId);
                request.setStatus(RequestStatus.REJECTED);
                ParticipationRequestDto participationRequestDto = requestMapper.toParticipationRequestDto(requestRepository.save(request));
                updatedRequests.add(participationRequestDto);
            }
            return new EventRequestStatusUpdateResult(Collections.emptyList(), updatedRequests);
        } else {
            for (Long requestId : requestIds) {
                Request request = getRequest(requestId);
                checkEventRequestLimit(event);
                request.setStatus(RequestStatus.CONFIRMED);
                updatedRequests.add(requestMapper.toParticipationRequestDto(requestRepository.save(request)));
            }
            return new EventRequestStatusUpdateResult(updatedRequests, Collections.emptyList());
        }
    }

    private void checkEventRequestLimit(Event event) {
        if (requestRepository.isParticipantLimitReached(event.getId(), event.getParticipantLimit())) {
            throw new ConflictException("Request limit reached");
        }
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