package ru.practicum.event.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

public enum EventAction {
    SEND_TO_REVIEW("PENDING"), // отправляется при публикации юзером

    CANCEL_REVIEW("CANCELED"), // отменяется юзером

    PUBLISH_EVENT("PUBLISHED"), // публикуется админом

    REJECT_EVENT("CANCELED"); // отменяется админом

    private final String value;

    EventAction(String value) {
        this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
        return String.valueOf(value);
    }

    @JsonCreator
    public static EventAction fromValue(@JsonProperty("stateAction") String text) {
        for (EventAction b : EventAction.values()) {
            if (String.valueOf(b.value).equals(text)) {
                return b;
            }
        }
        return null;
    }
}
