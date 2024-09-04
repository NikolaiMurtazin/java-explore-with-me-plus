package ru.practicum.request.model;

public enum RequestStatus {
    PENDING,

    REJECTED,

    CANCELED,

    CONFIRMED;


    public static RequestStatus fromValue(String statusString) {
        for (RequestStatus status : RequestStatus.values()) {
            if (status.name().equals(statusString)) {
                return status;
            }
            throw new IllegalArgumentException("Wrong status enum [" + statusString + "]");
        }
        return null;
    }
}