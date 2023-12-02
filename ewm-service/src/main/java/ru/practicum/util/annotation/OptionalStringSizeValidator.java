package ru.practicum.util.annotation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Optional;

public class OptionalStringSizeValidator implements ConstraintValidator<OptionalStringSize, Optional<String>> {

    private int min;
    private int max;

    @Override
    public void initialize(OptionalStringSize constraintAnnotation) {
        this.min = constraintAnnotation.min();
        this.max = constraintAnnotation.max();
    }

    @Override
    public boolean isValid(Optional<String> value, ConstraintValidatorContext context) {
        return value == null || (value.map(s -> s.length() >= min && s.length() <= max).orElse(true));
    }
}
