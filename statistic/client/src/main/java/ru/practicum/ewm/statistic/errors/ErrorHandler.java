package ru.practicum.ewm.statistic.errors;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.ewm.statistic.controller.StatisticClientController;
import ru.practicum.ewm.statistic.exeption.InvalidTimeRangeException;

import java.util.List;

@Slf4j
@RestControllerAdvice(assignableTypes = {StatisticClientController.class})

public class ErrorHandler {
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
    public ErrorResponse handleItemNotAvailableExceptionException(final InvalidTimeRangeException e) {
        return new ErrorResponse(e.getMessage());
    }


}
