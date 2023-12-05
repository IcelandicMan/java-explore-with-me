package ru.practicum.util.annotation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = OptionalLocalDateTimeValidator.class)
public @interface OptionalFutureOrPresent {
    String message() default "eventDate должна быть в настоящем или будущем времени";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
