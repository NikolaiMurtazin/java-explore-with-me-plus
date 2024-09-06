package ru.practicum.event.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import ru.practicum.event.model.Event;
import ru.practicum.user.model.User;

import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long>, QuerydslPredicateExecutor<Event> {

    Optional<Event> findByIdAndInitiator(long eventId, User user);

    List<Event> findByIdIn(List<Long> eventIds);

    boolean existsByCategoryId(long categoryId);

    @Query(value = "MERGE INTO event_likes (event_id, user_id) " +
            "VALUES(?1,?2)", nativeQuery = true)
    void operationLike(long eventId, long userId);

    @Query(value = "MERGE INTO event_dislikes (event_id, user_id) " +
            "VALUES(?1,?2)", nativeQuery = true)
    void operationDislike(long eventId, long userId);

    @Query(value = "DELETE FROM event_likes " +
            "WHERE event_id=?1 AND user_id=:?2", nativeQuery = true)
    void deleteLike(long eventId, long userId);

    @Query(value = "DELETE FROM event_dislikes " +
            "WHERE event_id=?1 AND user_id=?2", nativeQuery = true)
    void deleteDislike(long eventId, long userId);
}
