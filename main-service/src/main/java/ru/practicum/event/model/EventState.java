package ru.practicum.event.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

public enum EventState {
    SEND_TO_REVIEW("SEND_TO_REVIEW"), // отправляется при публикации юзером

    CANCEL_REVIEW("CANCEL_REVIEW"), // отменяется юзером

    PUBLISH_EVENT("PUBLISH_EVENT"), // публикуется админом

    REJECT_EVENT("REJECT_EVENT"), // отменяется админом

    PENDING("PENDING"),

    PUBLISHED("PUBLISHED"),

    CANCELED("CANCELED");

    private final String value;

    EventState(String value) {
        this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
        return String.valueOf(value);
    }

    @JsonCreator
    public static EventState fromValue(@JsonProperty("state") String text) {
        for (EventState b : EventState.values()) {
            if (String.valueOf(b.value).equals(text)) {
                return b;
            }
        }
        return null;
    }
}