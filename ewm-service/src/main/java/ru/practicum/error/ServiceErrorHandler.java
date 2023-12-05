package ru.practicum.error;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.category.controller.CategoryAdminController;
import ru.practicum.category.controller.CategoryPublicController;
import ru.practicum.category.exception.CategoryNotFoundException;
import ru.practicum.compilation.controller.CompilationAdminController;
import ru.practicum.compilation.controller.CompilationPublicController;
import ru.practicum.compilation.exception.CompilationNotFoundException;
import ru.practicum.event.controller.EventAdminController;
import ru.practicum.event.controller.EventPrivateController;
import ru.practicum.event.controller.EventPublicController;
import ru.practicum.event.exception.EventNotFoundException;
import ru.practicum.event.exception.EventStateException;
import ru.practicum.request.controller.RequestPrivateController;
import ru.practicum.request.exception.RequestNotOwnerException;
import ru.practicum.user.controller.UserAdminController;
import ru.practicum.user.exception.UserNotFoundException;
import ru.practicum.user.exception.UserNotOwnerException;
import ru.practicum.util.exception.ConflictException;
import ru.practicum.util.exception.ValidationException;

import java.util.List;

@Slf4j
@RestControllerAdvice(assignableTypes = {CategoryAdminController.class, CategoryPublicController.class,
        CompilationAdminController.class, CompilationPublicController.class,
        EventAdminController.class, EventPrivateController.class, EventPublicController.class,
        RequestPrivateController.class, UserAdminController.class})
public class ServiceErrorHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleIncorrectParameterException(final MethodArgumentNotValidException e) {
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        StringBuilder errorMessage = new StringBuilder("Ошибка валидации по параметру ");

        for (FieldError fieldError : fieldErrors) {
            errorMessage.append(fieldError.getField())
                    .append(" (значение: ")
                    .append(fieldError.getRejectedValue())
                    .append("): ")
                    .append(fieldError.getDefaultMessage())
                    .append(", ");
        }
        log.error(errorMessage.toString());
        return new ErrorResponse(errorMessage.toString());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationException(final ValidationException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleCategoryNotFoundException(final CategoryNotFoundException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleUserNotFoundException(final UserNotFoundException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleEventNotFoundException(final EventNotFoundException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleRequestNotOwnerException(final RequestNotOwnerException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleCompilationNotFoundException(final CompilationNotFoundException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleConflictException(final ConflictException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleUserNotOwnerException(final UserNotOwnerException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleEventStateException(final EventStateException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleEmailExistException(final ConstraintViolationException e) {
        return new ErrorResponse(e.getMessage());
    }
}
