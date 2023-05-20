package com.url.shortener.controller.exception;

import com.url.shortener.model.ApplicationError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(annotations = RestController.class)
public class ExceptionHandlerController {

    private static final Logger log = LoggerFactory.getLogger(ExceptionHandlerController.class);

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApplicationError> handleIllegalArgumentException(IllegalArgumentException exception) {
        return getResponse(exception, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApplicationError> handleRuntimeException(RuntimeException exception) {
        return getResponse(exception, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<ApplicationError> getResponse(RuntimeException exception, HttpStatus status) {
        ApplicationError error = new ApplicationError()
                .setCode(status.value())
                .setMessage(exception.getMessage());

        log.warn("Exception: {}, status: {}", exception, status);
        return new ResponseEntity<>(error, status);
    }
}
