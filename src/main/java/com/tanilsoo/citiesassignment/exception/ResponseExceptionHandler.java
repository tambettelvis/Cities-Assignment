package com.tanilsoo.citiesassignment.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@Slf4j
public class ResponseExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = { RuntimeException.class, CustomException.class})
    protected ResponseEntity<Object> handleConflict(RuntimeException ex, WebRequest request) {
        log.error(ex.getMessage());
        if (ex instanceof CustomException customException) {
            return handleExceptionInternal(customException,
                    ExceptionDto.builder()
                            .message(customException.getMessage())
                            .status_code(customException.getStatus())
                            .build(),
                    new HttpHeaders(), customException.getStatus(), request);
        }
        return handleExceptionInternal(ex, ex.getMessage(),
                new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

}
