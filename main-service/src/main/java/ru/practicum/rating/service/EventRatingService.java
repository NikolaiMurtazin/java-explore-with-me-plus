package ru.practicum.rating.service;

public interface EventRatingService {
    void addRating(long userId, long eventId, boolean isLike);

    void removeRating(long userId, long eventId, boolean isLike);
}
