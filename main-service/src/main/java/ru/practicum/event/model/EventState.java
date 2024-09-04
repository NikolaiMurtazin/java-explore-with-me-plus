package ru.practicum.event.model;

public enum EventState {

    PENDING,

    PUBLISHED,

    CANCELED;


    public static EventState fromValue(String eventStateString) {
        for (EventState state : EventState.values()) {
            if (state.name().equals(eventStateString)) {
                return state;
            }
            throw new IllegalArgumentException("Wrong eventState enum [" + eventStateString + "]");
        }
        return null;
    }
}