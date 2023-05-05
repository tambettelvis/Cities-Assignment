package com.tanilsoo.citiesassignment.exception;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatusCode;

@Builder
@Getter
public class ExceptionDto {
    private String message;
    private HttpStatusCode status_code;
}
