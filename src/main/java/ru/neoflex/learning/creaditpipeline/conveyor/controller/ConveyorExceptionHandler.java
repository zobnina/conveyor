package ru.neoflex.learning.creaditpipeline.conveyor.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.openapitools.model.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.neoflex.learning.creaditpipeline.conveyor.exception.PrescoringException;
import ru.neoflex.learning.creaditpipeline.conveyor.exception.ScoringException;

import java.time.OffsetDateTime;

@Slf4j
@RestControllerAdvice
public class ConveyorExceptionHandler {

    @ExceptionHandler(PrescoringException.class)
    @ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
    public ErrorMessage handlePrescoringException(HttpServletRequest request, PrescoringException e) {
        log.error("handlePrescoringException() - exception = {}", ExceptionUtils.getStackTrace(e));

        return getErrorMessage(request, HttpStatus.UNPROCESSABLE_ENTITY, e);
    }

    @ExceptionHandler(ScoringException.class)
    @ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
    public ErrorMessage handleScoringException(HttpServletRequest request, ScoringException e) {
        log.error("handleScoringException() - exception = {}", ExceptionUtils.getStackTrace(e));

        return getErrorMessage(request, HttpStatus.UNPROCESSABLE_ENTITY, e);
    }

    private ErrorMessage getErrorMessage(HttpServletRequest request, HttpStatus status, Exception e) {
        return ErrorMessage.builder()
            .timestamp(OffsetDateTime.now())
            .exception(e.getClass().getSimpleName())
            .status(status.value())
            .error(status.getReasonPhrase())
            .path(request.getRequestURI())
            .message(e.getLocalizedMessage())
            .build();
    }

}
