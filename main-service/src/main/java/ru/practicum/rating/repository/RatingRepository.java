package ru.practicum.rating.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import ru.practicum.event.model.Event;
import ru.practicum.rating.model.Rating;
import ru.practicum.user.model.User;

import java.util.Optional;

public interface RatingRepository extends JpaRepository<Rating, Long>, QuerydslPredicateExecutor<Rating> {
    Optional<Rating> findByUserAndEvent(User user, Event event);

    // Метод для подсчета количества лайков
    @Query("SELECT COUNT(r) FROM Rating r WHERE r.event = :event AND r.isLike = true")
    int countLikesByEvent(@Param("event") Event event);

    // Метод для подсчета количества дизлайков
    @Query("SELECT COUNT(r) FROM Rating r WHERE r.event = :event AND r.isLike = false")
    int countDislikesByEvent(@Param("event") Event event);
}
