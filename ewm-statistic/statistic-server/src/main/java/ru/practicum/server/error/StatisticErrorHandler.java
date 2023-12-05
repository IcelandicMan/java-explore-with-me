package ru.practicum.server.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.server.controller.StatisticController;
import ru.practicum.server.exeprion.InvalidTimeRangeException;

@Slf4j
@RestControllerAdvice(assignableTypes = {StatisticController.class})
public class StatisticErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationException(final InvalidTimeRangeException e) {
        return new ErrorResponse(e.getMessage());
    }

}
