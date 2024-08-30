package ru.practicum.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import ru.practicum.event.model.Event;
import ru.practicum.request.dto.EventCountByRequest;
import ru.practicum.request.model.Request;
import ru.practicum.request.model.RequestStatus;
import ru.practicum.user.model.User;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long>, QuerydslPredicateExecutor<Event> {

    List<Request> findByEventAndRequester(Event event, User user);

    List<Request> findByRequester(User user);

    List<Request> findByEventAndEvent_Initiator(Event event, User user);

    @Query(value = "SELECT r.event_id, COUNT(r.request_id) AS count " +
            "FROM requests r " +
            "JOIN events e ON r.event_id = e.event_id " +
            "WHERE r.event_id IN ?1 AND r.status = 'CONFIRMED' " +
            "GROUP BY r.event_id, e.participant_limit " +
            "HAVING COUNT(r.request_id) >= e.participant_limit",
            nativeQuery = true)
    List<EventCountByRequest> findConfirmedRequestWithLimitCheck(List<Long> eventIds);

    @Query(value = "SELECT r.event_id, COUNT(r.request_id) AS count " +
            "FROM requests r " +
            "WHERE r.event_id IN ?1 AND r.status = 'CONFIRMED' " +
            "GROUP BY r.event_id",
            nativeQuery = true)
    List<EventCountByRequest> findConfirmedRequestWithoutLimitCheck(List<Long> eventIds);


    @Query(value = "SELECT (COUNT(request_id)>=?2) FROM REQUESTS r " +
            "WHERE r.EVENT_ID = ?1 AND r.status= 'CONFIRMED'", nativeQuery = true)
    boolean isParticipantLimitReached(long eventId, int limit);

    @Query(value = "SELECT COUNT(r.request_id) AS count " +
            "FROM requests r " +
            "WHERE r.event_id = ?1 AND r.status = 'CONFIRMED' ",
            nativeQuery = true)
    Long countConfirmedRequest(long eventId);

    List<Request> findByEventAndRequesterAndStatusIn(Event event, User requester, List<RequestStatus> states);

    List<Request> findByEvent(Event event);
}