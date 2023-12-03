package ru.practicum.util.emum;

import ru.practicum.util.exception.ValidationException;

public enum Status {
    PENDING,
    CONFIRMED,
    CANCELED,
    REJECTED;

    public static Status getStatusValue(String status) {
        try {
            return Status.valueOf(status);
        } catch (Exception e) {
            throw new ValidationException("Неизвестный статус: " + status);
        }
    }
}