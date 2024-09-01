package ru.practicum.event.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum EventAction {

    SEND_TO_REVIEW("SEND_TO_REVIEW"), // отправляется при юзером

    CANCEL_REVIEW("CANCEL_REVIEW"), // отменяется юзером

    PUBLISH_EVENT("PUBLISH_EVENT"), // публикуется админом

    REJECT_EVENT("REJECT_EVENT"); // отменяется админом

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
    public static EventAction fromValue(String text) {
        for (EventAction b : EventAction.values()) {
            if (String.valueOf(b.value).equals(text)) {
                return b;
            }
        }
        return null;
    }
}
