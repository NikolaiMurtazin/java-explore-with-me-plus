package ru.practicum.request.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

public enum RequestStatus {
    PENDING("PENDING"),

    REJECTED("REJECTED"),

    CONFIRMED("CONFIRMED");

    private final String value;

    RequestStatus(String value) {
        this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
        return String.valueOf(value);
    }

    @JsonCreator
    public static RequestStatus fromValue(@JsonProperty("state") String text) {
        for (RequestStatus b : RequestStatus.values()) {
            if (String.valueOf(b.value).equals(text)) {
                return b;
            }
        }
        return null;
    }
}