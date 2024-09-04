package ru.practicum.event.dto;

public enum EventAction {

    SEND_TO_REVIEW, // отправляется при юзером

    CANCEL_REVIEW, // отменяется юзером

    PUBLISH_EVENT, // публикуется админом

    REJECT_EVENT; // отменяется админом

    public static EventAction fromValue(String textAction) {
        for (EventAction action : EventAction.values()) {
            if (action.name().equals(textAction)) {
                return action;
            }
        }
        throw new IllegalArgumentException("Unexpected value '" + textAction + "' to EventAction");
    }
}
