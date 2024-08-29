package ru.practicum.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import ru.practicum.event.model.Event;
import ru.practicum.request.model.Request;

public interface RequestRepository extends JpaRepository<Request, Long> , QuerydslPredicateExecutor<Event> {

}