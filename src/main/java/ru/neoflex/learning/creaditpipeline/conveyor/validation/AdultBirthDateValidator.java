package ru.neoflex.learning.creaditpipeline.conveyor.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import static java.util.Objects.nonNull;

public class AdultBirthDateValidator implements ConstraintValidator<AdultBirthDate, LocalDate> {
    @Override
    public void initialize(AdultBirthDate constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(LocalDate localDate, ConstraintValidatorContext constraintValidatorContext) {

        if (nonNull(localDate)) {
            final LocalDate now = LocalDate.now();
            final long age = ChronoUnit.YEARS.between(now, localDate);
            return age >= 18;
        }
        return true;
    }
}
