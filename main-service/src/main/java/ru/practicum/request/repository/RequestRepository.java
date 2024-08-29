package ru.practicum.request.repository;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import ru.practicum.event.model.Event;
import ru.practicum.request.model.Request;
import ru.practicum.user.model.User;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long>, QuerydslPredicateExecutor<Event> {

    List<Request> findByEventAndRequester(Event event, User user);

    List<Request> findByRequester(User user);

    List<Request> findByEventAndEvent_Initiator(Event event, User user);

    @Query(value = "SELECT r.event_id, COUNT(r.request_id) AS count, " +
            "CASE WHEN COUNT(r.request_id) >= e.participant_limit THEN TRUE ELSE FALSE END AS is_limit_reached " +
            "FROM requests r " +
            "JOIN events e ON r.event_id = e.event_id " +
            "WHERE r.event_id IN ?1 AND r.status = 'CONFIRMED' " +
            "GROUP BY r.event_id, e.participant_limit",
            nativeQuery = true)
    List<EventCountByRequest> findConfirmedRequestWithLimitCheck(List<Long> eventIds);


    @Query(value = "SELECT event_id, COUNT(request_id) FROM requests r " +
            "WHERE r.EVENT_ID IN ?1 AND r.status= 'CONFIRMED' GROUP BY event_id ", nativeQuery = true)
    List<EventCountByRequest> findConfirmedRequestWhereEventIn(List<Long> eventIds);

    @Query(value = "SELECT (COUNT(request_id)>=e.PARTICIPANT_LIMIT) FROM REQUESTS r, EVENTS e " +
            "WHERE r.EVENT_ID = ?1 AND r.status= 'CONFIRMED' GROUP BY e.EVENT_ID ", nativeQuery = true)
    boolean isParticipantLimitReached(long eventId);

    @Getter
    @Setter
    @AllArgsConstructor
    public class EventCountByRequest{
        long eventId;
        long count;
        boolean isLimitReached;
    }
}