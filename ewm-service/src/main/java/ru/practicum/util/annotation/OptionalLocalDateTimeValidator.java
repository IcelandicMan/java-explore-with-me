package ru.practicum.util.annotation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;
import java.util.Optional;

public class OptionalLocalDateTimeValidator implements ConstraintValidator<OptionalFutureOrPresent, Optional<LocalDateTime>> {

    @Override
    public boolean isValid(Optional<LocalDateTime> value, ConstraintValidatorContext context) {
        return value == null || (
                value.map(dateTime -> dateTime.isEqual(LocalDateTime.now()) ||
                        dateTime.isAfter(LocalDateTime.now())).orElse(true));
    }
}
