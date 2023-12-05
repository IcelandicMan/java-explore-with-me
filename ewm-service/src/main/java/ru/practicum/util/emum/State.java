package ru.practicum.util.emum;

import ru.practicum.util.exception.ValidationException;

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
