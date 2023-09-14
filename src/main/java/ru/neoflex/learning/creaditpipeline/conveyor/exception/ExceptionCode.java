package ru.neoflex.learning.creaditpipeline.conveyor.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ExceptionCode {

    AGE_NOT_VALID("Age not valid");

    private final String message;
}
