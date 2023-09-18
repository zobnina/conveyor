package ru.neoflex.learning.creaditpipeline.conveyor.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ExceptionCode {

    AGE_NOT_VALID("Age not valid"),
    UNEMPLOYED_STATUS("Unemployed status"),
    AMOUNT_TOO_BIG("Amount is greater than 20 salaries"),
    INAPPROPRIATE_AGE("Age is under 20 or greater than 60");

    private final String message;
}
