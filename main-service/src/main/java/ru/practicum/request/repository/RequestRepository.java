package ru.practicum.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import ru.practicum.event.model.Event;
import ru.practicum.request.model.Request;
import ru.practicum.user.model.User;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long>, QuerydslPredicateExecutor<Event> {

    List<Request> findByEventAndRequester(Event event, User user);

    List<Request> findByRequester(User user);

    List<Request> findByEventAndEvent_Initiator(Event event, User user);
}