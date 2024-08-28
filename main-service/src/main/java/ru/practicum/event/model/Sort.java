package ru.practicum.event.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Sort {
    EVENT_DATE("EVENT_DATE"),

    VIEWS("VIEWS");

    private String value;

    Sort(String value) {
        this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
        return String.valueOf(value);
    }

    @JsonCreator
    public static Sort fromValue(@JsonProperty("sort") String text) {
        for (Sort b : Sort.values()) {
            if (String.valueOf(b.value).equals(text)) {
                return b;
            }
        }
        return null;
    }
}
