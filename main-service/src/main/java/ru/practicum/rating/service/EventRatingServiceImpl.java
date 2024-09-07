package ru.practicum.rating.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.EventState;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exeption.ConflictException;
import ru.practicum.exeption.NotFoundException;
import ru.practicum.rating.model.EventRating;
import ru.practicum.rating.repository.EventRatingRepository;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EventRatingServiceImpl implements EventRatingService {

    private final EventRatingRepository ratingRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    @Transactional
    public void addRating(long userId, long eventId, boolean isLike) {
        User user = getUser(userId);
        Event event = getEvent(eventId);

        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new ConflictException("Cannot react because the event is not published");
        }

        Optional<EventRating> existingRating = ratingRepository.findByUserAndEvent(user, event);

        if (existingRating.isPresent()) {
            EventRating rating = existingRating.get();
            if (rating.getIsLike() == isLike) {
                throw new ConflictException((isLike ? "Like" : "Dislike") + " already exists");
            } else {
                rating.setIsLike(isLike);
                event.setLikeCount(event.getLikeCount() + (isLike ? 2 : -2));
                ratingRepository.save(rating);
            }
        } else {
            // Если нет реакции, создаем новую
            EventRating rating = EventRating.builder()
                    .user(user)
                    .event(event)
                    .isLike(isLike)
                    .build();
            ratingRepository.save(rating);
            event.setLikeCount(event.getLikeCount() + (isLike ? 1 : -1));
            eventRepository.save(event);
        }
    }

    @Transactional
    public void removeRating(long userId, long eventId, boolean isLike) {
        User user = getUser(userId);
        Event event = getEvent(eventId);

        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new ConflictException("Cannot remove reaction because the event is not published");
        }

        EventRating rating = getRating(user, event);

        if (rating.getIsLike() == isLike) {
            event.setLikeCount(event.getLikeCount() + (isLike ? -1 : 1));
            ratingRepository.delete(rating);
            eventRepository.save(event);
        } else {
            throw new ConflictException("No " + (isLike ? "like" : "dislike") + " found to remove");
        }
    }

    private User getUser(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found with id" + userId));
    }

    private Event getEvent(long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event not found with id: " + eventId));
    }

    private EventRating getRating(User user, Event event) {
        return ratingRepository.findByUserAndEvent(user, event)
                .orElseThrow(() -> new NotFoundException("Rating not found"));
    }
}
