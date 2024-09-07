package ru.practicum.rating.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.event.model.Event;
import ru.practicum.rating.model.EventRating;
import ru.practicum.user.model.User;

import java.util.Optional;

public interface EventRatingRepository extends JpaRepository<EventRating, Long> {
    Optional<EventRating> findByUserAndEvent(User user, Event event);
}
