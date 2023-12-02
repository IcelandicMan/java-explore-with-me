package ru.practicum.util.emums;

import ru.practicum.exception.ValidationException;

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