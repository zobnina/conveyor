package ru.neoflex.learning.creaditpipeline.conveyor.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ExceptionCode {

    AGE_NOT_VALID("Age not valid"),
    UNEMPLOYED_STATUS("Unemployed status"),
    AMOUNT_TOO_BIG("Amount is greater than 20 salaries"),
    INAPPROPRIATE_AGE("Age is under 20 or greater than 60"),
    INAPPROPRIATE_WORK_EXPERIENCE_TOTAL("Work experience total is less than 12 months"),
    INAPPROPRIATE_WORK_EXPERIENCE_CURRENT("Work experience current is less than 3 months");

    private final String message;
}
