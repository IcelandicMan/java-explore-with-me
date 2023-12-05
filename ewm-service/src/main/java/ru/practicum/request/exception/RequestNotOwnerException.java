package ru.practicum.request.exception;

public class RequestNotOwnerException extends RuntimeException {
    public RequestNotOwnerException(String message) {
        super(message);
    }
}
