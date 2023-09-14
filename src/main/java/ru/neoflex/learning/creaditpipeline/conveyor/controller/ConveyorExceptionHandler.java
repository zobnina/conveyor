package ru.neoflex.learning.creaditpipeline.conveyor.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.openapitools.model.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.neoflex.learning.creaditpipeline.conveyor.exception.PrescoringException;

import java.time.OffsetDateTime;

@Slf4j
@RestControllerAdvice
public class ConveyorExceptionHandler {

    @ExceptionHandler(PrescoringException.class)
    @ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY, reason = "Prescoring Error")
    public ResponseEntity<ErrorMessage> handlePrescoringException(HttpServletRequest request, PrescoringException e) {
        log.error("handlePrescoringException() - exception = {}", ExceptionUtils.getStackTrace(e));

        final ErrorMessage errorMessage = ErrorMessage.builder()
            .timestamp(OffsetDateTime.now())
            .exception(e.getClass().getSimpleName())
            .status(HttpStatus.UNPROCESSABLE_ENTITY.value())
            .error(HttpStatus.UNPROCESSABLE_ENTITY.getReasonPhrase())
            .path(request.getRequestURI())
            .message(e.getLocalizedMessage())
            .build();
        return ResponseEntity.unprocessableEntity().body(errorMessage);
    }
}
