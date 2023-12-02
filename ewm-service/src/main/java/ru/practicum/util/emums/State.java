package ru.practicum.util.emums;

import ru.practicum.exception.ValidationException;

public enum State {

    PENDING,
    PUBLISHED,
    CANCELED;

    public static State getStateValue(String state) {
        try {
            return State.valueOf(state);
        } catch (Exception e) {
            throw new ValidationException("Неизвестный статус: " + state);
        }
    }
}
