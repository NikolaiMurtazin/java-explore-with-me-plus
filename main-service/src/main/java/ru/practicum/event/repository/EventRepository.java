package ru.practicum.event.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import ru.practicum.event.model.Event;
import ru.practicum.user.model.User;

import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long>, QuerydslPredicateExecutor<Event> {

    @Query(value = "SELECT * FROM events e " +
            "WHERE e.annotation LIKE LOWER(CONCAT('%', :text, '%')) " +
            "OR e.description LIKE LOWER(CONCAT('%', :text, '%')) " +
            "AND e.CATEGORY_ID IN ?2 " +
            "AND e.PAID = ?3 " +
            "AND e.", nativeQuery = true)
    List<Event> findByAnnotation(String annotation);


    Optional<Event> findByIdAndInitiator(long eventId, User user);
}
