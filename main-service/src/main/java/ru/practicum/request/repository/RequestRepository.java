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

    @Query(value = "SELECT new ru.practicum.request.dto.EventCountByRequest(e.id, COUNT(r.id)) " +
            "FROM Event e " +
            "LEFT JOIN Request r ON r.event.id = e.id AND r.status = 'CONFIRMED' " +
            "WHERE e.id IN ?1 " +
            "GROUP BY e.id, e.participantLimit " +
            "HAVING COUNT(r.id) >= e.participantLimit")
    List<EventCountByRequest> findConfirmedRequestWithLimitCheck(List<Long> eventIds);

    @Query(value = "SELECT new ru.practicum.request.dto.EventCountByRequest(e.id, COUNT(r.id)) " +
            "FROM Event e " +
            "LEFT JOIN Request r ON r.event.id = e.id AND r.status = 'CONFIRMED' " +
            "WHERE e.id IN ?1 " +
            "GROUP BY e.id")
        //TODO
    List<EventCountByRequest> findConfirmedRequestWithoutLimitCheck(List<Long> eventIds);


    @Query(value = "SELECT (COUNT(request_id)>=?2) FROM REQUESTS r " +
            "WHERE r.EVENT_ID = ?1 AND r.status= 'CONFIRMED'", nativeQuery = true)
    boolean isParticipantLimitReached(long eventId, int limit);

    @Query(value = "SELECT COUNT(r.request_id) AS count " +
            "FROM requests r " +
            "WHERE r.event_id = ?1 AND r.status = 'CONFIRMED' ",
            nativeQuery = true)
    Integer countConfirmedRequest(long eventId);


    List<Request> findByEventAndRequesterAndStatusIn(Event event, User requester, List<RequestStatus> states);

    List<Request> findByEvent(Event event);
}