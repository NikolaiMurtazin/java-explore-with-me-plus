package ru.practicum.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exeption.NotFoundException;
import ru.practicum.request.dto.ParticipationRequestDto;
import ru.practicum.request.dto.RequestParams;
import ru.practicum.request.mapper.RequestMapper;
import ru.practicum.request.model.Request;
import ru.practicum.request.repository.RequestRepository;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly=true)
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;

    private final RequestMapper requestMapper;

    private final UserRepository userRepository;
    private final EventRepository eventRepository;


    public List<ParticipationRequestDto> findUserRequests(long userId){
        User user = getUser(userId);
        List<Request> allRequsts = requestRepository.findAllByUser(user);

        if (allRequsts.isEmpty()){
            return List.of();
        }
        return allRequsts.stream().map(requestMapper::toDto).toList();
    }


    @Transactional
    public ParticipationRequestDto save(RequestParams params){
        getUser(params.getUserId);
        Request saved =getRequest(params.getRequestId);

        requestMapper.save(requestMapper.toEntity(saved));

        return requestMapper.toDto(saved);
        //TODO check limit

    }

    @Transactional
    public ParticipationRequestDto cancel(long userId, long requestId){
        getUser(userId);
        Request request = getRequest(requestId);
        request.setStatus(Status.PENDING);


        return requestMapper.toDto(saved);
    }

    public List<ParticipationRequestDto> findUserRequestOnEvent(long userId, long eventId){
        User user = getUser(userId);
        Event event = getEvent(eventId);
        List<Request> allRequsts = requestRepository.findAllByUserAndByEvent(user,event);
        if (allRequsts.isEmpty()){
            return List.of();
        }
        return allRequsts.stream().map(requestMapper::toDto).toList();
    }


    private Request getRequest(long requestId) {
        return requestRepository.findById(requestId).orElseThrow(()->new NotFoundException("Request not found with id" + requestId));
    }

    private User getUser(long userId) {
        return userRepository.findById(requestId).orElseThrow(()->new NotFoundException("User not found with id" + userId));
    }

    private Event getEvent (long eventId) {
        return eventRepository.findById(requestId).orElseThrow(()->new NotFoundException("User not found with id" + eventId));
    }


}