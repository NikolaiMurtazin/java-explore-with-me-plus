package ru.practicum.event.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import ru.practicum.event.model.Event;

import java.util.List;

public interface EventsRepository extends JpaRepository<Event, Long>, QuerydslPredicateExecutor<Event> {

    @Query(value = "SELECT * FROM events e " +
            "WHERE e.annotation LIKE LOWER(CONCAT('%', :text, '%')) " +
            "OR e.description LIKE LOWER(CONCAT('%', :text, '%')) " +
            "AND e.CATEGORY_ID IN ?2 " +
            "AND e.PAID = ?3 " +
            "AND e.", nativeQuery = true)
    public List<Event> findByAnnotation(String annotation);
}
